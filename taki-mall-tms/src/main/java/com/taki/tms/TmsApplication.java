package com.taki.tms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName TmsApplication
 * @Description TMS 服务 启动类
 * @Author Long
 * @Date 2022/5/17 15:40
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.taki.common.config","com.taki.tms.*"})
@EnableDiscoveryClient
public class TmsApplication {

    public static void main(String[] args) {
        SpringApplication.run(TmsApplication.class,args);
    }
}
