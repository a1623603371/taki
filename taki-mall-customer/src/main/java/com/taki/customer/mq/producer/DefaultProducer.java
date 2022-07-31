package com.taki.customer.mq.producer;

import com.taki.common.constants.RocketMQConstant;
import com.taki.common.mq.MQMessage;
import com.taki.customer.exception.CustomerBizException;
import com.taki.customer.exception.CustomerErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.autoconfigure.RocketMQProperties;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName DefaultProducer
 * @Description TODO
 * @Author Long
 * @Date 2022/7/31 13:59
 * @Version 1.0
 */
@Component
@Slf4j
public class DefaultProducer {


    private DefaultMQProducer defaultMQProducer;

    public DefaultProducer(RocketMQProperties rocketMQProperties) {
        this.defaultMQProducer = new DefaultMQProducer(RocketMQConstant.ORDER_DEFAULT_PRODUCER_GROUP);
       defaultMQProducer.setNamesrvAddr(rocketMQProperties.getNameServer());

    }

    public DefaultMQProducer getDefaultMQProducer(){return defaultMQProducer;}
    
    /** 
     * @description:  对象在使用之前必须调用一次，只能初始一次
     * @param 
     * @return  void
     * @author Long
     * @date: 2022/7/31 14:08
     */ 
    public void start() {
        try {
            this.defaultMQProducer.start();
        } catch (MQClientException e) {
            log.error("producer  start  error",e);
        }
    }
    
    /** 
     * @description:  一般应用上下文，使用上下文监听器，进行关闭
     * @param
     * @return  void
     * @author Long
     * @date: 2022/7/31 14:11
     */ 
    public void shutdown(){

        this.defaultMQProducer.shutdown();
        
    }

    /**
     * @description: 发送消息
     * @param topic 消息主题
     * @param  message 消息主体
     * @param  type
     * @return  void
     * @author Long
     * @date: 2022/7/31 14:12
     */
    public void sendMessage(String topic,String message,String type){
        sendMessage(topic,message,-1,type);
    }

    /**
     * @description: 发送消息
     * @param topic 消息主题
     * @param  message 消息主体
     *  @param delayTimeLevel 延时时间等级
     * @param  type
     * @return  void
     * @author Long
     * @date: 2022/7/31 14:12
     */
    public void sendMessage(String topic,String message,Integer delayTimeLevel,String type){
        Message msg = new MQMessage(topic,message.getBytes(StandardCharsets.UTF_8));
        try {
            if (delayTimeLevel != 0){
                msg.setDelayTimeLevel(delayTimeLevel);
            }
            SendResult sendResult = defaultMQProducer.send(msg);
            if (SendStatus.SEND_OK == sendResult.getSendStatus()){
                log.info("发送MQ消息成功，type:{},message:{}",type,message);
            }else{
                throw new CustomerBizException(sendResult.getSendStatus().toString());
            }

        }catch (Exception e){
            log.error("发送MQ消息失败",e);
            throw new CustomerBizException(CustomerErrorCodeEnum.SEND_MQ_FAILED);
        }
    }

}
