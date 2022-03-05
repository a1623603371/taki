package com.taki.fulfill.mq.producer;

import com.taki.common.constants.RocketMQConstant;
import com.taki.fulfill.mq.config.RocketMQProperties;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName DefaultProducer
 * @Description 默认的普通的mq消息生产者 （只能发普通消息，不能发事务消息）
 * @Author Long
 * @Date 2022/3/5 10:35
 * @Version 1.0
 */
@Component
public class DefaultProducer {

    private static Logger log = LoggerFactory.getLogger(DefaultProducer.class);


    private DefaultMQProducer producer;

    @Autowired
    public DefaultProducer(RocketMQProperties rocketMQProperties) {
        producer = new DefaultMQProducer(RocketMQConstant.ORDER_DEFAULT_PRODUCER_GROUP);
        producer.setNamesrvAddr(rocketMQProperties.getNameServer());
        start();
    }
    
    /** 
     * @description: 启动 生产者
     * @param 
     * @return  void
     * @author Long
     * @date: 2022/3/5 10:43
     */ 
    private void start() {

        try {
            producer.start();
        } catch (MQClientException e) {
            log.error("producer  start  error",e);
        }
    }


    public void shutdown(){
        producer.shutdown();
    }

    public DefaultMQProducer getProducer() {
        return producer;
    }
}
