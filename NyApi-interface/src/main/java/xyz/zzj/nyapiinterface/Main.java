package xyz.zzj.nyapiinterface;

import xyz.zzj.nyapiinterface.client.NyApiClient;
import xyz.zzj.nyapiinterface.model.User;

/**
 * @BelongsPackage: xyz.zzj.nyapiinterface
 * @ClassName: Main
 * @Author: zengz
 * @CreateTime: 2024/2/23 20:10
 * @Description: TODO 描述类的功能
 * @Version: 1.0
 */
public class Main {
    public static void main(String[] args) {

        String accessKey = "nianyan";
        String secretKey = "qwertyuiop";

        NyApiClient nyApiClient = new NyApiClient(accessKey,secretKey);
        String nianyan1 = nyApiClient.getName("nianyan1");
        String nianyan2 = nyApiClient.getNameByPost("nianyan2");
        User user = new User();
        user.setUserName("nianyan3");
        String nianyan3 = nyApiClient.getUserNameByPost(user);
        System.out.println(nianyan1);
        System.out.println(nianyan2);
        System.out.println(nianyan3);
    }
}
