package com.taki.sso;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName SSOApplication
 * @Description 单点登录 服务
 * @Author Long
 * @Date 2021/12/16 15:22
 * @Version 1.0
 */
@SpringBootApplication
public class SSOApplication {


    public static void main(String[] args) {
        SpringApplication.run(SSOApplication.class,args);
    }
}
