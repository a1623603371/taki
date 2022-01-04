package com.taki.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @ClassName OrderApplication
 * @Description 订单服务 启动类
 * @Author Long
 * @Date 2022/1/4 10:27
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.taki.common","com.taki.order"})
public class OrderApplication {


    public static void main(String[] args) {
        SpringApplication.run(OrderApplication.class,args);
    }



}
