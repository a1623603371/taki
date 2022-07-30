package com.taki.fulfill;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName FulfillApplication
 * @Description TODO
 * @Author Long
 * @Date 2022/3/4 15:49
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.taki.common","com.taki.fulfill"})
@EnableDiscoveryClient
public class FulfillApplication {

    public static void main(String[] args) {
        SpringApplication.run(FulfillApplication.class,args);
    }
}
