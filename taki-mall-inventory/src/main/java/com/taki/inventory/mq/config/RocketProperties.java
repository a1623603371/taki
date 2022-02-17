package com.taki.inventory.mq.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName RocketProperties
 * @Description rocket配置
 * @Author Long
 * @Date 2022/2/17 13:55
 * @Version 1.0
 */
@Data
@ConfigurationProperties(prefix = "rocketmq")
public class RocketProperties {

    private String nameServer;


}
