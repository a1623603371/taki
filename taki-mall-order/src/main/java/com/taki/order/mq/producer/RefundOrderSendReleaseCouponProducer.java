package com.taki.order.mq.producer;

import com.taki.common.constants.RocketMQConstant;
import com.taki.order.mq.config.RocketMQProperties;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName RefundOrderSendReleaseCouponProducer
 * @Description 退款订单发送释放优惠券 producer 组件
 * @Author Long
 * @Date 2022/6/8 20:16
 * @Version 1.0
 */
@Component
public class RefundOrderSendReleaseCouponProducer extends AbstractTransactionProducer{

    @Autowired
    public RefundOrderSendReleaseCouponProducer(RocketMQProperties rocketMQProperties) {

        transactionMQProducer = new TransactionMQProducer(RocketMQConstant.RELEASE_PROPERTY_PRODUCER_GROUP);
        transactionMQProducer.setNamesrvAddr(rocketMQProperties.getNameServer());
        start();
    }
}
