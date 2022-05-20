package com.taki.fulfill.mq.consumer;

import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.constants.RocketMQConstant;
import com.taki.fulfill.domain.request.TriggerOrderWmsShipEventRequest;
import com.taki.fulfill.mq.config.RocketMQProperties;
import com.taki.fulfill.mq.consumer.listener.CancelFulfillTopicListener;
import com.taki.fulfill.mq.consumer.listener.TriggerOrderFulfillTopicListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ConsumerConfig
 * @Description TODO
 * @Author Long
 * @Date 2022/5/20 17:37
 * @Version 1.0
 */
@Configuration
public class ConsumerConfig {

    @Autowired
    private RocketMQProperties rocketMQProperties;

    /** 
     * @description: 触发订单履约消息消费者
     * @param listener
     * @return  org.apache.rocketmq.client.consumer.DefaultMQPushConsumer
     * @author Long
     * @date: 2022/5/20 17:46
     */ 
    @Bean("triggerOrderFulfillConsumer")
    public DefaultMQPushConsumer triggerOrderFulfillConsumer(TriggerOrderFulfillTopicListener listener) throws MQClientException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQConstant.TRIGGER_ORDER_FULFILL_CONSUMER_GROUP);

        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(RocketMQConstant.TRIGGER_ORDER_FULFILL_TOPIC,"*");
        consumer.registerMessageListener(listener);
        consumer.start();

        return consumer;

    }

    /** 
     * @description: 取消 履约消息消费者
     * @param listener
     * @return  org.apache.rocketmq.client.consumer.DefaultMQPushConsumer
     * @author Long
     * @date: 2022/5/20 17:47
     */ 
    @Bean("cancelFulfillConsumer")
    public DefaultMQPushConsumer cancelFulfillConsumer(CancelFulfillTopicListener listener) throws MQClientException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQConstant.CANCEL_FULFILL_CONSUMER_GROUP);

        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(RocketMQConstant.CANCEL_FULFILL_TOPIC,"*");
        consumer.registerMessageListener(listener);
        consumer.start();
        return consumer;

    }
}
