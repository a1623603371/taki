package com.taki.push.mq.producer;

import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.constants.RocketMQConstant;
import com.taki.push.exception.PushBizException;
import com.taki.push.mq.config.RocketMQProperties;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName DefaultPrducer
 * @Description TODO
 * @Author Long
 * @Date 2022/10/11 17:45
 * @Version 1.0
 */
@Slf4j
@Component
public class DefaultProducer {

    private final TransactionMQProducer producer;


    public DefaultProducer(RocketMQProperties rocketMQProperties) {
        this.producer = new TransactionMQProducer(RocketMQConstant.PUSH_DEFAULT_PRODUCER_GROUP);
        producer.setNamesrvAddr(rocketMQProperties.getNameServer());
        
        start();
    }
    /*** 
     * @description:  对象使用之前必须调用一次，只初始化一次
     * @param 
     * @return  void
     * @author Long
     * @date: 2022/10/11 17:48
     */ 
    private void start() {

        try {
            this.producer.start();
        } catch (MQClientException e) {
            log.error("producer start error",e);
        }
    }

    /*** 
     * @description:   一般在应用上下文，使用上下文监听器，进行关闭
     * @param 
     * @return  void
     * @author Long
     * @date: 2022/10/11 17:52
     */ 
    public void shutdown(){
        this.producer.shutdown();
    }


    public TransactionMQProducer getProducer(){
        return this.producer;
    }

    /*** 
     * @description: 批量发送消息
     * @param topic 主题
     * @param  messages 多个消息
     * @param  delayTimeLevel 延时时间等级
     * @param type 类型
     * @return  void
     * @author Long
     * @date: 2022/10/11 17:55
     */ 
    public void sendMessages(String topic, List<String> messages,Integer  delayTimeLevel,String type){

        List<Message> list = new ArrayList<>();

        messages.forEach(msg ->{
            Message message = new Message(topic,msg.getBytes(StandardCharsets.UTF_8));
            if (delayTimeLevel > 0){
                message.setDelayTimeLevel(delayTimeLevel);
            }
            list.add(message);
        });

        try {
            SendResult sendResult = producer.send(list);

            if (SendStatus.SEND_OK == sendResult.getSendStatus()){
                log.info("发送MQ消息发送成功，type:{}",type);
            }else{
                throw new PushBizException(sendResult.getSendStatus().toString());
            }

        }catch (Exception e){
            log.error("发送MQ消息失败:",e);

            throw new PushBizException("消息发送失败");

        }
        
    }

    /***
     * @description: 批量 发送消息
     * @param topic 主题
     * @param messages 消息集合
     * @param type 消息类型
     * @return  void
     * @author Long
     * @date: 2022/10/11 18:09
     */
    public void sendMessages(String topic,List<String>  messages,String type){

        sendMessages(topic,messages,-1,type);
    }

    /***
     * @description:  发送消息
     * @param topic 消息主题
     * @param   message
     * @param  delayTimeLevel
     * @param  type
     * @return  void
     * @author Long
     * @date: 2022/10/11 18:11
     */
    public void sendMessage(String topic,String message,Integer delayTimeLevel,String type){

    Message msg = new Message(topic,message.getBytes(StandardCharsets.UTF_8));

    try {
        if (delayTimeLevel > 0){
            msg.setDelayTimeLevel(delayTimeLevel);
        }

        SendResult sendResult = producer.send(msg);

        if (SendStatus.SEND_OK == sendResult.getSendStatus()){
            log.info("发送消息成功,message={},type={}",message,type);
        }else {
            throw new PushBizException(sendResult.getSendStatus().toString());
        }


    }catch (Exception e){
        log.info("发送MQ消息失败:{}",e);

        throw new PushBizException("发送MQ消息失败");
    }

    }

    /***
     * @description:  发送消息
     * @param topic 消息 主题
     * @param  message 消息
     * @param type 消息类型
     * @return  void
     * @author Long
     * @date: 2022/10/11 18:15
     */
    public void sendMessage(String topic,String message,String type){
        sendMessage(topic,message,-1,type);
    }
}
