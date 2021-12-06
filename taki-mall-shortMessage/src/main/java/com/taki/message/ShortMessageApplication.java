package com.taki.message;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @ClassName ShortMessageApplication
 * @Description 短信发送服务
 * @Author Long
 * @Date 2021/12/4 14:14
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.taki.core","com.taki.message"})
@EnableEurekaClient
public class ShortMessageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ShortMessageApplication.class,args);
    }
}
