package com.taki.order.mq.producer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.TransactionMQProducer;

/**
 * @ClassName AbstractTransactionProducer
 * @Description TODO
 * @Author Long
 * @Date 2022/6/8 18:46
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractTransactionProducer {

    protected TransactionMQProducer transactionMQProducer;


    /** 
     * @description: 对象在使用之前必须调用一次，只能初始化一次， 启动 事务消息 生产者
     * @param 
     * @return  void
     * @author Long
     * @date: 2022/6/8 18:48
     */ 
    public void start(){
        try {
            this.transactionMQProducer.start();
        } catch (MQClientException e) {
            log.error("producer  start error={}",e);
        }

    }

    /** 
     * @description: 一般在应用上下文，使用应用上下文监听器进行关闭
     * @param 
     * @return  void
     * @author Long
     * @date: 2022/6/8 18:50
     */ 
    public void  shutdown(){
        this.transactionMQProducer  .shutdown();
    }

    public TransactionMQProducer getTransactionMQProducer() {
        return transactionMQProducer;
    }
}
