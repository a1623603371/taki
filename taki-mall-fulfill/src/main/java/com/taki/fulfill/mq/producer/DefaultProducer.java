package com.taki.fulfill.mq.producer;

import com.taki.common.constants.RocketMQConstant;
import com.taki.fulfill.exection.FulfillBizException;
import com.taki.fulfill.exection.FulfillErrorCodeEnum;
import com.taki.fulfill.mq.config.RocketMQProperties;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

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

    /** 
     * @description:  一般在应用上下文，使用上下文监听器 进行关闭
     * @param
     * @return  void
     * @author Long
     * @date: 2022/5/15 18:18
     */ 
    public void shutdown(){


        producer.shutdown();
    }


    /*** 
     * @description: 发送消息
     * @param topic 主题
     * @param message 消息
     * @param type 消息类型
     * @return  void
     * @author Long
     * @date: 2022/5/15 18:19
     */ 
    public  void sendMessage(String topic,String message,String type){
        sendMessage(topic,message,-1,type);
    }


    /** 
     * @description:  发送消息
     * @param topic 主题
     * @param message 消息
     * @param delayTimeLevel
     * @param type
     * @return  void
     * @author Long
     * @date: 2022/5/15 18:20
     */ 
    public  void sendMessage(String topic,String message,Integer delayTimeLevel,String type){

        Message msg = new Message(topic,message.getBytes(StandardCharsets.UTF_8));
        try {

            if (delayTimeLevel > 0){
                msg.setDelayTimeLevel(delayTimeLevel);
            }
            SendResult sendResult = producer.send(msg);
            if (SendStatus.SEND_OK ==  sendResult.getSendStatus()){
                log.info("发送 MQ 消息 成功，type:{} message:{}",type,message);

            }else{
                throw new FulfillBizException(sendResult.getSendStatus().toString());
            }


        }catch (Exception e){
            log.error("发送MQ消息失败：",e);
            throw new FulfillBizException(FulfillErrorCodeEnum.SEND_MQ_FAILED);
        }

        
    }

    public DefaultMQProducer getProducer() {
        return producer;
    }
}
