package com.taki.order.mq.producer;

import com.taki.order.mq.config.RocketMQProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName DefaultProducer
 * @Description 默认的普通的mq 生产者 （只发送普通消息，不能发送事务消息）
 * @Author Long
 * @Date 2022/1/12 10:18
 * @Version 1.0
 */
@Slf4j
@Component
public class DefaultProducer {


    private DefaultProducer producer;


    public DefaultProducer(RocketMQProperties rocketMQProperties) {
     //   this.producer =
    }
}
