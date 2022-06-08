package com.taki.order.mq.producer;

import com.taki.common.constants.RocketMQConstant;
import com.taki.order.mq.config.RocketMQProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName AfterSaleApplySendActualRefundProducer
 * @Description 售后申请流程发送实际退款producer组件
 * @Author Long
 * @Date 2022/6/8 18:52
 * @Version 1.0
 */
@Slf4j
@Component
public class AfterSaleApplySendActualRefundProducer extends AbstractTransactionProducer{

    @Autowired
    public AfterSaleApplySendActualRefundProducer(RocketMQProperties rocketMQProperties) {
        transactionMQProducer = new TransactionMQProducer(RocketMQConstant.ACTUAL_REFUND_CONSUMER_GROUP);
        transactionMQProducer.setNamesrvAddr(rocketMQProperties.getNameServer());
        this.start();
    }
}
