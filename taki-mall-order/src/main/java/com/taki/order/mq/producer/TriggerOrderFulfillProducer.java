package com.taki.order.mq.producer;

import com.taki.common.constants.RocketMQConstant;
import com.taki.order.mq.config.RocketMQProperties;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.stereotype.Component;

/**
 * @ClassName TriggerOrderFulfillProducer
 * @Description 触发 订单履约 生产者组件
 * @Author Long
 * @Date 2022/6/8 20:19
 * @Version 1.0
 */
@Component
public class TriggerOrderFulfillProducer extends AbstractTransactionProducer{

    public TriggerOrderFulfillProducer(RocketMQProperties rocketMQProperties) {
        transactionMQProducer = new TransactionMQProducer(RocketMQConstant.TRIGGER_ORDER_FULFILL_PRODUCER_GROUP);
        transactionMQProducer.setNamesrvAddr(rocketMQProperties.getNameServer());
        start();
    }

}
