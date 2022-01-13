package com.taki.order.mq.producer;

import com.taki.common.constants.RocketMQConstant;
import com.taki.order.exception.OrderBizException;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.mq.config.RocketMQProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * @ClassName DefaultProducer
 * @Description 默认的普通的mq 生产者 （只发送普通消息，不能发送事务消息）
 * @Author Long
 * @Date 2022/1/12 10:18
 * @Version 1.0
 */
@Slf4j
@Component
public class DefaultProducer {


    private final DefaultMQProducer producer;


    public DefaultProducer(RocketMQProperties rocketMQProperties) {
     //   this.producer =
        this.producer = new DefaultMQProducer(RocketMQConstant.ORDER_DEFAULT_PRODUCER_GROUP);
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

    /** 
     * @description: 发送消息
     * @param topic 消息主题
     * @param message 消息
     * @param type 消息类型
     * @return  void
     * @author Long
     * @date: 2022/1/13 22:08
     */ 
    public void  sendMessage(String topic,String message,String type){

    }

    /**
     * @description: 发送消息
     * @param topic 主题
     * @param message 消息
     * @param delayTimeLevel 延时时间等级
     * @param type 消息类型
     * @return  void
     * @author Long
     * @date: 2022/1/13 22:10
     */
    public void sendMessage(String topic,String message,Integer delayTimeLevel,String type){

        Message msg = new Message(topic,message.getBytes(StandardCharsets.UTF_8));

        try {
            if (delayTimeLevel > 0){
                msg.setDelayTimeLevel(delayTimeLevel);
            }
            SendResult sendResult = producer.send(msg);

            if (SendStatus.SEND_OK == sendResult.getSendStatus()){
                log.info("发送MQ消息成功， type:{},message:{}",type,message);
            }else {
                throw new OrderBizException(sendResult.getSendStatus().toString());
            }

        }catch (Exception e){
            log.error("发送消息失败:{}",e);
            throw new OrderBizException(OrderErrorCodeEnum.SEND_MQ_FAILED);
        }

    }
}
