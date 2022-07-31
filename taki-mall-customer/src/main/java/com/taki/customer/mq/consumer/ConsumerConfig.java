package com.taki.customer.mq.consumer;

import com.taki.common.constants.RocketMQConstant;
import com.taki.customer.mq.consumer.listener.AfterSaleCustomerAuditTopicListener;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ConsumerConfig
 * @Description TODO
 * @Author Long
 * @Date 2022/7/31 14:25
 * @Version 1.0
 */
@Configuration
public class ConsumerConfig {

    private RocketMQProperties rocketMQProperties;


    /*** 
     * @description:  客服接受售后申请消费者
     * @param 
     * @return
     * @author Long
     * @date: 2022/7/31 14:28
     */ 
    @Bean("afterSaleCustomerAudit")
    public DefaultMQPushConsumer afterSaleCustomerAudit(AfterSaleCustomerAuditTopicListener afterSaleCustomerAuditTopicListener) throws MQClientException {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer(RocketMQConstant.AFTER_SALE_CUSTOMER_AUDIT_GROUP);
        defaultMQPushConsumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        defaultMQPushConsumer.subscribe(RocketMQConstant.AFTER_SALE_CUSTOMER_AUDIT_TOPIC,"*");
        defaultMQPushConsumer.registerMessageListener(afterSaleCustomerAuditTopicListener);
        defaultMQPushConsumer.start();
        return defaultMQPushConsumer;

    }
}
