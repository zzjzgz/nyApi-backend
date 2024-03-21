package xyz.zzj.nyapiinterface;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("xyz.zzj.nyapiinterface.mapper")
public class NyApiInterfaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NyApiInterfaceApplication.class, args);
    }

}
