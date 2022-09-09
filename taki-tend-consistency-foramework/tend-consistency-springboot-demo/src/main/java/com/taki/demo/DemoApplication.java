package com.taki.demo;

import com.taki.consistency.annotation.EnableTendConsistencyTask;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @ClassName DemoApplication
 * @Description TODO
 * @Author Long
 * @Date 2022/9/4 22:02
 * @Version 1.0
 */
@SpringBootApplication(scanBasePackages = {"com.taki.*"})
@EnableScheduling
@EnableTendConsistencyTask
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class,args);
    }
}
