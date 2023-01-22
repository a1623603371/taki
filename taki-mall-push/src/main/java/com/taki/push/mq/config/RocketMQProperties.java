package com.taki.push.mq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName RocketMQProperties
 * @Description TODO
 * @Author Long
 * @Date 2022/10/9 23:28
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "rocketmq")
@Data
public class RocketMQProperties {

    private String nameServer;


}
