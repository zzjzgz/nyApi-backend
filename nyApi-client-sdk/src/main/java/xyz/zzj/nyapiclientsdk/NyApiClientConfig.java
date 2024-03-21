package xyz.zzj.nyapiclientsdk;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import xyz.zzj.nyapiclientsdk.client.NyApiClient;

/**
 * @BelongsPackage: xyz.zzj.nyapiclientsdk
 * @ClassName: NyApiClientConfig
 * @Author: zengz
 * @CreateTime: 2024/3/5 18:09
 * @Description: API接口客户端
 * @Version: 1.0
 */
@Configuration
@ConfigurationProperties("nyapi.client")
@Data
@ComponentScan
public class NyApiClientConfig {

    private String accessKey;

    private String secretKey;

    @Bean
    public NyApiClient nyApiClient(){
        return new NyApiClient(accessKey,secretKey);
    }


}
