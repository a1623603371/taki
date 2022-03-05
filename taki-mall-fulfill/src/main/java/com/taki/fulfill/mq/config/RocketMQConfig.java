package com.taki.fulfill.mq.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RocketMQConfig
 * @Description TODO
 * @Author Long
 * @Date 2022/3/5 10:25
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties(RocketMQProperties.class)
public class RocketMQConfig {
}
