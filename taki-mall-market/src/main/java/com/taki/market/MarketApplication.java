package com.taki.market;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName MarketApplication
 * @Description
 * @Author Long
 * @Date 2022/2/18 10:18
 * @Version 1.0
 */
@SpringBootApplication
@EnableDiscoveryClient
public class MarketApplication {
    public static void main(String[] args) {
        SpringApplication.run(MarketApplication.class,args);
    }
}
