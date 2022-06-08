package com.taki.order.mq.producer;

import com.taki.common.constants.RocketMQConstant;
import com.taki.order.mq.config.RocketMQProperties;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.stereotype.Component;

/**
 * @ClassName PaidOrderSuccessProducer
 * @Description 支付订单成功生产者组件
 * @Author Long
 * @Date 2022/6/8 20:10
 * @Version 1.0
 */
@Component
public class PaidOrderSuccessProducer extends AbstractTransactionProducer{

    public PaidOrderSuccessProducer(RocketMQProperties rocketMQProperties) {
        transactionMQProducer = new TransactionMQProducer(RocketMQConstant.PAID_ORDER_SUCCESS_PRODUCER_GROUP);
        transactionMQProducer.setNamesrvAddr(rocketMQProperties.getNameServer());
        start();


    }
}
