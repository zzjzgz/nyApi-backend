package xyz.zzj.nyapigateway;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import xyz.zzj.common.model.entity.NyApiInterface;
import xyz.zzj.common.model.entity.User;
import xyz.zzj.common.service.InnerNyApiInterfaceService;
import xyz.zzj.common.service.InnerNyApiUserInterfaceService;
import xyz.zzj.common.service.InnerUserService;
import xyz.zzj.nyapiclientsdk.utils.SignUtils;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * 全局过滤
 */
@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {

    @DubboReference
    private InnerUserService innerUserService;
    @DubboReference
    private InnerNyApiInterfaceService innerNyApiInterfaceService;

    @DubboReference
    private InnerNyApiUserInterfaceService innerUserInterfaceInfoService;

    /**
     * IP白名单列表
     */
    private final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");

    /**
     * 模拟接口地址，因为本项目只有一个模拟接口，所以直接写死
     */
    private static final String INTERFACE_HOST = "http://localhost:8123";

    /**
     *
     * @param exchange 路由交换机
     * @param chain 链条
     * @return
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        1. 用户发送请求到api网关
//        2. 记入日志
        ServerHttpRequest request = exchange.getRequest();
        String url = INTERFACE_HOST + request.getPath().value();
        String method = request.getMethod().toString();
        if (StringUtils.isBlank(url) || StringUtils.isBlank(method)){
            return handleNoAuth(exchange.getResponse());
        }
        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径：" + url);
        log.info("请求方法：" + method);
        log.info("请求参数：" + request.getQueryParams());
        String hostString = Objects.requireNonNull(request.getLocalAddress()).getHostString();
        log.info("请求来源地址：" + hostString);
//        3. 访问控制 - （黑白名单）
        ServerHttpResponse response = exchange.getResponse();
        if (!IP_WHITE_LIST.contains(hostString)){
            return handleNoAuth(response);
        }
//        4. 用户鉴权--> 判断ak / sk是否合法
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String body = headers.getFirst("body");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        //判断用户传来的参数是否为空
        if (StringUtils.isBlank(accessKey) || StringUtils.isBlank(nonce) || StringUtils.isBlank(body) || StringUtils.isBlank(timestamp) || StringUtils.isBlank(sign)){
            return handleNoAuth(response);
        }
        //查询用户的accessKey值和secretKey值，在进行比较
        //判断accessKey的用户是否存在
        User userInfo = null;
        //这里需要捕获由getUserInfo方法抛出的异常
        try{
            userInfo = innerUserService.getUserInfo(accessKey);
        }catch (Exception e){
            log.error("getUserInfo error", e);
        }
        if (userInfo == null) {
            return handleNoAuth(response);
        }
        //对随机数进行校验
        if (Long.parseLong(nonce) > 10000) {
            return handleNoAuth(response);
        }
        //对时间进行校验，时间不超过当前时间的5分钟
        long now = System.currentTimeMillis() / 1000;
        long longValue = Optional.of(Long.parseLong(timestamp)).orElse(0L);
        final long FIVE_MINUTES = 60 * 5L;
        if ((now - longValue) >= FIVE_MINUTES){
            return handleNoAuth(response);
        }
        //使用的是数据库查询出来的secretKey
        String secretKey = userInfo.getSecretKey();
        String serverSign = SignUtils.getSign(body, secretKey);
        //对签名进行校验,secretKey为空和数据库中签名不一致事，返回无权限
        if (!sign.equals(serverSign)) {
            return handleNoAuth(response);
        }
        //5. 请求模拟接口是否存在
        //从数据库中查询模拟接口是否存在，以及请求方法是否匹配
        NyApiInterface interfaceInfo = null;
        try{
            interfaceInfo = innerNyApiInterfaceService.getInterfaceInfo(url, method);
        }catch (Exception e){
            log.error("getInterfaceInfo error", e);
        }
        if (interfaceInfo == null){
            return handleNoAuth(response);
        }
        //todo 用户是否该接口的有调用次数
        //6. 调用模拟接口
//        Mono<Void> filter = chain.filter(exchange);
//        7. 记入响应日志
        Long interfaceInfoId = interfaceInfo.getId();
        Long userId = userInfo.getId();
        return handleResponse(exchange,chain,interfaceInfoId,userId);
    }

    /**
     * 处理无权限访问
     * @param response 返回无权限响应
     * @return
     */
    private static Mono<Void> handleNoAuth(ServerHttpResponse response) {
        //给响应值设置一个响应状态码，403拒绝访问
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    /**
     * 处理调用错误
     * @param response 返回错误信息
     * @return
     */
    private static Mono<Void> handleInvokeError(ServerHttpResponse response) {
        //给响应值设置一个响应状态码，500
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    /**
     * 处理响应值，请求转发，调用模拟接口
     * @param exchange
     * @param chain
     * @return
     */
    public Mono<Void> handleResponse(ServerWebExchange exchange, GatewayFilterChain chain,long interfaceInfoId, long userId){
        try {
            ServerHttpResponse originalResponse = exchange.getResponse();
            //缓冲区工厂，
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            //拿到响应码
            HttpStatus statusCode = originalResponse.getStatusCode();
            if (statusCode == HttpStatus.OK) {
                // 装饰，增强能力
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {
                    // 等调用完转发的接口后才会执行
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        log.info("body instanceof Flux: {}", (body instanceof Flux));
                        if (body instanceof Flux|| body instanceof Mono) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);
                            // 往返回值里写数据
                            // 拼接字符串
                            return super.writeWith(
                                fluxBody.map(dataBuffer -> {
                                    // 7. 调用成功，接口调用次数 + 1 invokeCount，并使用try-catch包裹来获取异常
                                    try {
                                        innerUserInterfaceInfoService.invokeCount(interfaceInfoId, userId);
                                    } catch (Exception e) {
                                        log.error("invokeCount error", e);
                                    }
                                    byte[] content = new byte[dataBuffer.readableByteCount()];
                                    dataBuffer.read(content);
                                    DataBufferUtils.release(dataBuffer);//释放掉内存
                                    // 构建日志
                                    StringBuilder sb2 = new StringBuilder(200);
                                    List<Object> rspArgs = new ArrayList<>();
                                    rspArgs.add(originalResponse.getStatusCode());
                                    String data = new String(content, StandardCharsets.UTF_8); //data
                                    sb2.append(data);
                                    // 打印日志
                                    log.info("响应结果：" + data);
                                    return bufferFactory.wrap(content);
                                }));
                        } else {
                            // 8. 调用失败，返回一个规范的错误码
                            log.error("<--- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 设置 response 对象为装饰过的
                return chain.filter(exchange.mutate().response(decoratedResponse).build());
            }
            return chain.filter(exchange); // 降级处理返回数据
        } catch (Exception e) {
            log.error("网关处理响应异常" + e);
            return chain.filter(exchange);
        }
    }



    @Override
    public int getOrder() {
        return -1;
    }
}