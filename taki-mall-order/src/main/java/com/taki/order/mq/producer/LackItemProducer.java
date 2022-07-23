package com.taki.order.mq.producer;

import com.taki.common.constants.RocketMQConstant;
import com.taki.order.mq.config.RocketMQProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName LackItemProducer
 * @Description 缺品处理的生产者组件
 * @Author Long
 * @Date 2022/6/8 20:05
 * @Version 1.0
 */
@Slf4j
@Component
public class LackItemProducer extends AbstractTransactionProducer{


    @Autowired
    public LackItemProducer(RocketMQProperties rocketMQProperties) {
        transactionMQProducer = new TransactionMQProducer(RocketMQConstant.LACK_ITEM_PRODUCER_GROUP);
        transactionMQProducer.setNamesrvAddr(rocketMQProperties.getNameServer());
        start();

    }
}
