package com.taki.careerplan;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * @ClassName CookBookApplication
 * @Description TODO
 * @Author Long
 * @Date 2023/2/18 16:40
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.taki.common.*"})
@EnableDiscoveryClient
public class CookBookApplication {
}
