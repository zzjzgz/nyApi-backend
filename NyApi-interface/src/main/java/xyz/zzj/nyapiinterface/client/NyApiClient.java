package xyz.zzj.nyapiinterface.client;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import xyz.zzj.nyapiinterface.model.User;

import java.util.HashMap;
import java.util.Map;

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
        System.out.println(result);
        return result;
    }

    public String getNameByPost(@RequestParam String name){
        //可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        String result= HttpUtil.post("http://localhost:8123/api/name/", paramMap);
        System.out.println(result);
        return result;
    }

    private Map<String,String> getHeadersMap(){
        Map<String,String> map = new HashMap<>();
        map.put("accessKey",accessKey);
        map.put("secretKey",secretKey);
        return map;
    }

    public String getUserNameByPost(@RequestBody User user){
        String json = JSONUtil.toJsonStr(user);
        HttpResponse httpResponse = HttpRequest.post("http://localhost:8123/api/name/user")
                .addHeaders(getHeadersMap())
                .body(json)
                .execute();
        System.out.println(httpResponse.getStatus());
        String body = httpResponse.body();
        return body;
    }
}
