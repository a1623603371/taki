package com.taki.wms;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName WmsApplication
 * @Description TODO
 * @Author Long
 * @Date 2022/5/16 20:19
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.taki.common.config","com.taki.wms.*"})
@EnableDiscoveryClient
public class WmsApplication {


    public static void main(String[] args) {
        SpringApplication.run(WmsApplication.class,args);
    }
}
