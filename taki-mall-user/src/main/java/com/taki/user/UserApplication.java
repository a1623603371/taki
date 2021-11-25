package com.taki.user;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * @ClassName UserApplication
 * @Description 用户服务启动类
 * @Author Long
 * @Date 2021/11/23 17:58
 * @Version 1.0
 */

@SpringBootApplication(scanBasePackages = {"com.taki.core.*","com.taki.user"})
@EnableOpenApi
public class UserApplication {

    public static void main(String[] args) {

        SpringApplication.run(UserApplication.class,args);
       // new  SpringApplicationBuilder().bannerMode(Banner.Mode.OFF).sources(UserApplication.class).run(args);
    }
}
