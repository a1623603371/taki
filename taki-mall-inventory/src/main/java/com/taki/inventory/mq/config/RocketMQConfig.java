package com.taki.inventory.mq.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName RocketMQConfig
 * @Description rocketMQ 配置 信息
 * @Author Long
 * @Date 2022/2/17 11:58
 * @Version 1.0
 */
@Configuration
@EnableConfigurationProperties(RocketProperties.class)
public class RocketMQConfig {
}
