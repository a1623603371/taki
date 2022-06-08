package com.taki.order.mq.producer;

import com.taki.common.constants.RocketMQConstant;
import com.taki.order.mq.config.RocketMQProperties;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName CustomerAuditPassSendReleaseAssetsProducer
 * @Description 客服审核通过发送释放权益资产 producer 组件
 * @Author Long
 * @Date 2022/6/8 19:57
 * @Version 1.0
 */
@Component
public class CustomerAuditPassSendReleaseAssetsProducer extends  AbstractTransactionProducer {


    @Autowired
    public CustomerAuditPassSendReleaseAssetsProducer(RocketMQProperties rocketMQProperties) {

        transactionMQProducer = new TransactionMQProducer(RocketMQConstant.CUSTOMER_AUDIT_PASS_RELEASE_ASSETS_PRODUCER_GROUP);
        transactionMQProducer.setNamesrvAddr(rocketMQProperties.getNameServer());
        start();
    }
}
