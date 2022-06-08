package com.taki.order.mq.producer;

import com.taki.common.constants.RocketMQConstant;
import com.taki.order.mq.config.RocketMQProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName CancelOrderSendPreparationProducer
 * @Description 取消订单流程发送前置准备工作 producer 组件
 * @Author Long
 * @Date 2022/6/8 18:55
 * @Version 1.0
 */
@Slf4j
@Component
public class CancelOrderSendPreparationProducer extends AbstractTransactionProducer {
    @Autowired
    public CancelOrderSendPreparationProducer(RocketMQProperties rocketMQProperties) {

        transactionMQProducer = new TransactionMQProducer(RocketMQConstant.ACTUAL_REFUND_CONSUMER_GROUP);
        transactionMQProducer.setNamesrvAddr(rocketMQProperties.getNameServer());
        start();

    }
}
