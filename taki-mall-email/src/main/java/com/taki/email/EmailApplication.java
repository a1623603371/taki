package com.taki.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName EmailApplication
 * @Description 邮件发送服务
 * @Author Long
 * @Date 2021/12/10 14:05
 * @Version 1.0
 */

@SpringBootApplication
public class EmailApplication {


    public static void main(String[] args) {
        SpringApplication.run(EmailApplication.class,args);
    }
}
