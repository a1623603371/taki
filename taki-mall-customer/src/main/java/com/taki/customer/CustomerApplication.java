package com.taki.customer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName CustomerApplication
 * @Description TODO
 * @Author Long
 * @Date 2022/7/30 19:56
 * @Version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class CustomerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomerApplication.class,args);
    }
}
