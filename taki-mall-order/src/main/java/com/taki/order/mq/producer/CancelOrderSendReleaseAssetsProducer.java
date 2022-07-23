package com.taki.order.mq.producer;

import com.taki.common.constants.RocketMQConstant;
import com.taki.order.mq.config.RocketMQProperties;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName CancelOrderSendReleaseAssetsProducer
 * @Description 取消订单流程发送权益资产 producer 组件
 * @Author Long
 * @Date 2022/6/8 19:05
 * @Version 1.0
 */
@Component
public class CancelOrderSendReleaseAssetsProducer extends AbstractTransactionProducer{

    @Autowired
    public CancelOrderSendReleaseAssetsProducer(RocketMQProperties rocketMQProperties) {

        transactionMQProducer = new TransactionMQProducer(RocketMQConstant.RELEASE_ASSETS_PRODUCER_GROUP);
        transactionMQProducer.setNamesrvAddr(rocketMQProperties.getNameServer());
        start();

    }
}
