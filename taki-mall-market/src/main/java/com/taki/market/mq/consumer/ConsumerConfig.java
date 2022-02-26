package com.taki.market.mq.consumer;

import com.taki.common.constants.RocketMQConstant;
import com.taki.market.mq.config.RocketMQProperties;
import com.taki.market.mq.consumer.listener.ReleasePropertyListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ConsumerConfig
 * @Description  消费者配置
 * @Author Long
 * @Date 2022/2/26 16:46
 * @Version 1.0
 */
@Configuration
public class ConsumerConfig {

    @Autowired
    private RocketMQProperties rocketMQProperties;


    @Bean("releaseInventoryConsumer")
    public DefaultMQPushConsumer releaseInventoryConsumer(ReleasePropertyListener releasePropertyListener) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQConstant.RELEASE_PROPERTY_CONSUMER_GROUP);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(RocketMQConstant.CANCEL_RELEASE_PROPERTY_TOPIC,"*");
        consumer.registerMessageListener(releasePropertyListener);
        consumer.start();
        return consumer;

    }


}
