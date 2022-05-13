package com.taki.order.mq.consumer;

import com.taki.common.constants.RocketMQConstant;
import com.taki.order.mq.config.RocketMQProperties;
import com.taki.order.mq.consumer.listener.*;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ConsumerConfig
 * @Description 消费者配置
 * @Author Long
 * @Date 2022/5/13 14:22
 * @Version 1.0
 */
@Configuration
public class ConsumerConfig {


    @Autowired
    private RocketMQProperties rocketMQProperties;

    /** 
     * @description:  订单完成支付 消费者
     * @param paidOrderSuccessListener
     * @return  org.apache.rocketmq.client.consumer.DefaultMQPushConsumer
     * @author Long
     * @date: 2022/5/13 15:01
     */ 

    @Bean("paidOrderSuccessConsumer")
    public DefaultMQPushConsumer paidOrderSuccessConsumer(PaidOrderSuccessListener  paidOrderSuccessListener) throws MQClientException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQConstant.PAID_ORDER_SUCCESS_CONSUMER_GROUP);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(RocketMQConstant.PAID_ORDER_SUCCESS_TOPIC,"*");
        consumer.registerMessageListener(paidOrderSuccessListener);
        consumer.start();
        return consumer;
    }


    /** 
     * @description: 实际退款 消费者
     * @param actualRefundListener  实际退款 mq 监听
     * @return
     * @author Long
     * @date: 2022/5/13 15:02
     */ 
    @Bean("actualRefundConsumer")
    public DefaultMQPushConsumer actualRefundConsumer(ActualRefundListener actualRefundListener) throws MQClientException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQConstant.ACTUAL_REFUND_CONSUMER_GROUP);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(RocketMQConstant.ACTUAL_REFUND_TOPIC,"*");
        consumer.registerMessageListener(actualRefundListener);
        consumer.start();
        return consumer;
    }

    /** 
     * @description: 消费者退款 请求消息 消费者
     * @param cancelRefundListener 消费者取消退款 监听
     * @return
     * @author Long
     * @date: 2022/5/13 15:05
     */ 
    @Bean("cancelRefundConsumer")
    public  DefaultMQPushConsumer cancelRefundConsumer(CancelRefundListener cancelRefundListener) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQConstant.REQUEST_CONSUMER_GROUP);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(RocketMQConstant.CANCEL_REFUND_REQUEST_TOPIC,"*");
        consumer.registerMessageListener(cancelRefundListener);
        consumer.start();
        return consumer;
    }

    /** 
     * @description: 订单物流配送结果rocketmq 消费者
     * @param orderWmsShipResultListener 订单物流配送结果监听器
     * @return
     * @author Long
     * @date: 2022/5/13 15:09
     */ 
    @Bean("orderWmsShipResultConsumer")
    public  DefaultMQPushConsumer orderWmsShipResultConsumer(OrderWmsShipResultListener orderWmsShipResultListener) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQConstant.ORDER_WMS_SHIP_RESULT_CONSUMER_GROUP);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(RocketMQConstant.ORDER_WMS_SHIP_RESULT_TOPIC,"*");
        consumer.registerMessageListener(orderWmsShipResultListener);
        consumer.start();
        return consumer;
    }

    /** 
     * @description: 支付订单 超时延迟消息消费者
     * @param payOrderTimeoutListener  支付订单 超时延迟消息 监听
     * @return  org.apache.rocketmq.client.consumer.DefaultMQPushConsumer
     * @author Long
     * @date: 2022/5/13 15:30
     */ 
    @Bean("payOrderTimeoutConsumer")
    public DefaultMQPushConsumer payOrderTimeoutConsumer(PayOrderTimeoutListener payOrderTimeoutListener) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQConstant.PAY_ORDER_TIME_DELAY_CONSUMER_GROUP);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(RocketMQConstant.PAY_ORDER_TIMEOUT_DELAY_TOPIC,"*");
        consumer.registerMessageListener(payOrderTimeoutListener);
        consumer.start();
        return consumer;
    }

    /** 
     * @description: 释放资产消息消费者
     * @param releaseAssetsListener 释放资产消息 监听器
     * @return
     * @author Long
     * @date: 2022/5/13 15:35
     */
    @Bean("releaseAssetsConsumer")
    public  DefaultMQPushConsumer releaseAssetsConsumer(ReleaseAssetsListener releaseAssetsListener) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQConstant.RELEASE_ASSETS_CONSUMER_GROUP);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(RocketMQConstant.RELEASE_ASSETS_TOPIC,"*");
        consumer.registerMessageListener(releaseAssetsListener);
        consumer.start();
        return consumer;
    }

    /**
     * @description: 审核通过释放资产
     * @param auditPassReleaseAssetsListens 审核通过释放资产监听器
     * @return 审核通过释放资产 消费者
     * @author Long
     * @date: 2022/5/13 17:13
     */
    @Bean("auditPassReleaseAssetsConsumer")
    public DefaultMQPushConsumer auditPassReleaseAssetsConsumer(AuditPassReleaseAssetsListens auditPassReleaseAssetsListens) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQConstant.CUSTOMER_AUDIT_PASS_RELEASE_ASSETS_CONSUMER_GROUP);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(RocketMQConstant.CUSTOMER_AUDIT_PASS_RELEASE_ASSETS_TOPIC,"*");
        consumer.registerMessageListener(auditPassReleaseAssetsListens);
        consumer.start();
        return consumer;
    }
}
