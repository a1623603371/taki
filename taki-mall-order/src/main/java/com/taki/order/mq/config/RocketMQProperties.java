package com.taki.order.mq.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName RocketMQProperties
 * @Description TODO
 * @Author Long
 * @Date 2022/1/12 10:16
 * @Version 1.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "rocketmq")
public class RocketMQProperties {

    private String nameServer;


}
