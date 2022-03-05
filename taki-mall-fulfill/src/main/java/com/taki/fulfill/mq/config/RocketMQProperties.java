package com.taki.fulfill.mq.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @ClassName RocketMQProperties
 * @Description TODO
 * @Author Long
 * @Date 2022/3/5 10:26
 * @Version 1.0
 */
@ConfigurationProperties(prefix = "rocketmq")
public class RocketMQProperties {

    private String nameServer;

    public String getNameServer() {
        return nameServer;
    }

    public void setNameServer(String nameServer) {
        this.nameServer = nameServer;
    }
}
