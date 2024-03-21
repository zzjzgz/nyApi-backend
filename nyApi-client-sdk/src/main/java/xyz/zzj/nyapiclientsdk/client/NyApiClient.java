package xyz.zzj.nyapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import xyz.zzj.nyapiclientsdk.model.User;

import java.util.HashMap;
import java.util.Map;

import static xyz.zzj.nyapiclientsdk.utils.SignUtils.getSign;

/**
 * @BelongsPackage: xyz.zzj.nyapiinterface.client
 * @ClassName: NyApiClient
 * @Author: zengz
 * @CreateTime: 2024/2/23 20:00
 * @Description: 调用api的接口
 * @Version: 1.0
 */
public class NyApiClient {

    private String accessKey;
    private String secretKey;

    public NyApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String getName(String name){
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
         String result= HttpUtil.get("http://localhost:8123/api/name/", paramMap);
//        System.out.println(result);
        return result;
    }

    public String getNameByPost(String name){
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result= HttpUtil.post("http://localhost:8123/api/name/", paramMap);
//        System.out.println(result);
        return result;
    }

    private Map<String,String> getHeadersMap(String body){
        Map<String,String> map = new HashMap<>();
        map.put("accessKey",accessKey);
        //这个参数一定不能发送
//        map.put("secretKey",secretKey);
        map.put("body",body);
        map.put("timestamp",String.valueOf(System.currentTimeMillis() / 1000));
        map.put("nonce", RandomUtil.randomNumbers(4));
        map.put("sign",getSign(body,secretKey));
        return map;
    }

    public String getUserNameByPost(User user){
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post("http://localhost:8123/api/name/user")
                .addHeaders(getHeadersMap(json))
                .body(json)
                .execute();
        System.out.println(httpResponse.getStatus());
        String body = httpResponse.body();
        return body;
    }
}
