package com.taki.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName GateWayApplication
 * @Description gateWay 网关 启动入口
 * @Author Long
 * @Date 2022/6/14 18:57
 * @Version 1.0
 */
@EnableDiscoveryClient
@SpringBootApplication
public class GateWayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GateWayApplication.class,args);
    }

}
