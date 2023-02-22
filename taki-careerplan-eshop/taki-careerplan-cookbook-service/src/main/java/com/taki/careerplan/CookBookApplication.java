package com.taki.careerplan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.ApplicationContext;

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

    public static void main(String[] args) {
        ApplicationContext context =    SpringApplication.run(CookBookApplication.class,args);

      //  new SpringApplicationBuilder().sources(CookBookApplication.class).context().run(args);
    }
}
