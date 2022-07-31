package com.taki.address;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName AddressApplication
 * @Description 用户收货地址服务 启动类
 * @Author Long
 * @Date 2022/7/31 16:18
 * @Version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class AddressApplication {

    public static void main(String[] args) {
        SpringApplication.run(AddressApplication.class,args);
    }
}
