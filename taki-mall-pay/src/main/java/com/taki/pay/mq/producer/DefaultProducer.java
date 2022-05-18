package com.taki.pay.mq.producer;

import com.taki.common.constants.RocketMQConstant;

import com.taki.pay.exception.PayBizException;
import com.taki.pay.exception.PayErrorCodeEnum;
import com.taki.pay.mq.config.RocketMQProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName DefaultProducer
 * @Description 发送事务消息
 * @Author Long
 * @Date 2022/1/12 10:18
 * @Version 1.0
 */
@Slf4j
@Component
public class DefaultProducer {


    private final TransactionMQProducer producer;


    public DefaultProducer(RocketMQProperties rocketMQProperties) {
     //   this.producer =
        this.producer = new TransactionMQProducer(RocketMQConstant.ORDER_DEFAULT_PRODUCER_GROUP);
        producer.setNamespace(rocketMQProperties.getNameServer());
        start();
    }
    /*** 
     * @description:  生产者启动
     * @param 
     * @return  void
     * @author Long
     * @date: 2022/1/13 22:05
     */ 
    private void start() {
        try {
            producer.start();
        }catch (Exception e){
        log.error("生产者启动失败:{}",e);
        }
    }


    /**
     * @description: 关闭生产者
     * @param
     * @return  void
     * @author Long
     * @date: 2022/1/13 22:08
     */
    public void shutdown(){
            producer.shutdown();
    }





    public TransactionMQProducer getProducer() {

        return producer;
    }


}
