package com.taki.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName OrderProperties
 * @Description 订单配置
 * @Author Long
 * @Date 2022/1/6 11:18
 * @Version 1.0
 */
@Configuration
@Data
@ConfigurationProperties(prefix = "eshop.order")
public class OrderProperties {

    /**
     *订单超时支付限制，默认30分钟
     */
    private Integer expireTime = 30 * 60 * 1000;


    private static final  Integer ORDER_EXPIRE_TIME = 30 * 60 * 1000;
}
