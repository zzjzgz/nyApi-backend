package xyz.zzj.nyapiinterface;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import xyz.zzj.nyapiclientsdk.client.NyApiClient;
import xyz.zzj.nyapiclientsdk.model.User;

import javax.annotation.Resource;

@SpringBootTest
class NyApiInterfaceApplicationTests {

    @Resource
    private NyApiClient nyApiClient;

    @Test
    void contextLoads() {
        String zzj = nyApiClient.getName("zzj");
        System.out.println(zzj);
        User user = new User();
        user.setUserName("zzj1");
        String userNameByPost = nyApiClient.getUserNameByPost(user);
        System.out.println(userNameByPost);
        String zzj2 = nyApiClient.getNameByPost("zzj2");
        System.out.println(zzj2);
    }

}
