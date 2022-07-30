package com.taki.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import sun.java2d.pipe.AATextRenderer;

/**
 * @ClassName ProductApplication
 * @Description 商品服务 入口
 * @Author Long
 * @Date 2022/2/17 15:29
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.taki.product"})
@EnableDiscoveryClient
public class ProductApplication {

    public static void main(String[] args) {

        SpringApplication.run(ProductApplication.class, args);
    }
}
