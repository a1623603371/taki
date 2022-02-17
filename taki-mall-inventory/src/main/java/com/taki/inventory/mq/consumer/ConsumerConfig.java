package com.taki.inventory.mq.consumer;

import com.taki.common.constants.RocketMQConstant;
import com.taki.inventory.mq.config.RocketProperties;
import com.taki.inventory.mq.consumer.listener.ReleaseInventoryListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ConsumerConfig
 * @Description rocketMQ 消费者配置
 * @Author Long
 * @Date 2022/2/17 14:04
 * @Version 1.0
 */
@Configuration
public class ConsumerConfig {

    @Autowired
    private RocketProperties rocketProperties;

    /** 
     * @description: 释放库存消费者
     * @param 
     * @return  org.apache.rocketmq.client.consumer.DefaultMQPushConsumer
     * @author Long
     * @date: 2022/2/17 14:07
     */ 
    @Bean("releaseInventoryConsumer")
    public DefaultMQPushConsumer releaseInventoryConsumer(ReleaseInventoryListener releaseInventoryListener) throws MQClientException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQConstant.RELEASE_INVENTORY_CONSUMER_GROUP);
        consumer.setNamesrvAddr(rocketProperties.getNameServer());
        consumer.subscribe(RocketMQConstant.CANCEL_RELEASE_INVENTORY_TOPIC,"*");
        consumer.registerMessageListener(releaseInventoryListener);
        consumer.start();
        return consumer;
    }
}
