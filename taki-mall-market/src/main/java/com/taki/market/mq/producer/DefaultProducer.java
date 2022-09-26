package com.taki.market.mq.producer;

import com.taki.common.constants.RocketMQConstant;
import com.taki.common.mq.MQMessage;
import com.taki.market.exception.MarketBizException;
import com.taki.market.mq.config.RocketMQProperties;
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
 * @ClassName DefaultProducer
 * @Description 默认 生产者
 * @Author Long
 * @Date 2022/9/26 22:05
 * @Version 1.0
 */
@Slf4j
@Component
public class DefaultProducer {


    private  final TransactionMQProducer producer;

    public DefaultProducer(RocketMQProperties rocketMQProperties) {
        this.producer = new TransactionMQProducer(RocketMQConstant.PUSH_DEFAULT_PRODUCER_GROUP);
        this.producer.setCompressMsgBodyOverHowmuch(Integer.MAX_VALUE);
        this.producer.setVipChannelEnabled(true);
        this.producer.setNamesrvAddr(rocketMQProperties.getNameServer());

        start();
    }


    /*** 
     * @description: 在对象使用之前要调用一次，只能初始化一次
     * @param 
     * @return  void
     * @author Long
     * @date: 2022/9/26 22:10
     */ 
    public void start(){
        try {
            this.producer.start();
        }catch (MQClientException e){
        log.error("producer start  error",e);
        }
    }

    /*** 
     * @description:  在应用上下文，使用上下文监听器 进行关闭
     * @param 
     * @return  void
     * @author Long
     * @date: 2022/9/26 22:11
     */ 
    public void shutdown(){

       this.producer.shutdown();
    }

    /***
     * @description:  发送消息
     * @param topic 消息topic
     * @param message 消息数据
     * @paramtype
     * @return  void
     * @author Long
     * @date: 2022/9/26 22:13
     */
    public void sendMessage(String topic,String message,String type){

        sendMessage(topic,message,-1,type);

    }

    /***
     * @description:  发送消息
     * @param topic 消息topic
     * @param message 消息数据
     * @paramtype
     * @return  void
     * @author Long
     * @date: 2022/9/26 22:13
     */
    public void sendMessage(String topic,String message,Integer delayTimeLevel,String type){
        Message msg = new MQMessage(topic,message.getBytes(StandardCharsets.UTF_8));

        try {
            if(delayTimeLevel > 0){
                msg.setDelayTimeLevel(delayTimeLevel);
            }
            SendResult sendResult = producer.send(msg);

            if (SendStatus.SEND_OK == sendResult.getSendStatus()){
                log.info("发送MQ消息成功，type:{},message:{}",type,message);
            }else {
                throw new MarketBizException(sendResult.getSendStatus().toString());
            }

        }catch (Exception e){
        log.error("发送MQ消息失败：type:{}",e);

        throw new MarketBizException("消息发送失败");
        }
    }



    /***
     * @description:  发送消息
     * @param topic 消息topic
     * @param messages 消息数据
     * @paramtype
     * @return  void
     * @author Long
     * @date: 2022/9/26 22:13
     */
    public void sendMessages(String topic, List<String> messages,String type){
        sendMessages(topic,messages,-1,type);
    }
    /***
     * @description:  发送消息
     * @param topic 消息topic
     * @param messages 消息数据
     * @paramtype
     * @return  void
     * @author Long
     * @date: 2022/9/26 22:13
     */
    public void sendMessages(String topic, List<String> messages,String type,Long timeoutMills){
        sendMessages(topic,messages,-2,timeoutMills,type);
    }

    /***
     * @description:  发送消息
     * @param topic 消息topic
     * @param messages 消息数据
     * @paramtype
     * @return  void
     * @author Long
     * @date: 2022/9/26 22:13
     */
    public void sendMessages(String topic,List<String> messages,Integer delayTimeLevel,String type){

        List<Message> msgList = new ArrayList<>();
        messages.forEach(msg->{
        Message message = new MQMessage(topic,msg.getBytes(StandardCharsets.UTF_8));

        if (delayTimeLevel > 0){

            message.setDelayTimeLevel(delayTimeLevel);
        }
        msgList.add(message);
        });

        try {
            SendResult sendResult = producer.send(msgList);
            if (SendStatus.SEND_OK.equals(sendResult.getSendStatus())){
                log.info("发送 MQ 成功，type:{}",type);
            }else {
                throw new MarketBizException(sendResult.getSendStatus().toString());
            }
        }catch (Exception e) {
            log.error("发送MQ失败：type：{}",type);
            throw new MarketBizException("消息发送失败");
        }

    }

    /***
     * @description:  发送消息
     * @param topic 消息topic
     * @param messages 消息数据
     * @paramtype
     * @return  void
     * @author Long
     * @date: 2022/9/26 22:13
     */
    public void sendMessages(String topic,List<String> messages,Integer delayTimeLevel,Long timeoutMills,String type){

        List<Message> msgList = new ArrayList<>();
        messages.forEach(msg->{
            Message message = new MQMessage(topic,msg.getBytes(StandardCharsets.UTF_8));

            if (delayTimeLevel > 0){

                message.setDelayTimeLevel(delayTimeLevel);
            }
            msgList.add(message);
        });

        try {
            SendResult sendResult = producer.send(msgList,timeoutMills);
            if (SendStatus.SEND_OK.equals(sendResult.getSendStatus())){
                log.info("发送 MQ 成功，type:{}",type);
            }else {
                throw new MarketBizException(sendResult.getSendStatus().toString());
            }
        }catch (Exception e) {
            log.error("发送MQ失败：type：{}",type);
            throw new MarketBizException("消息发送失败");
        }

    }

    public TransactionMQProducer getProducer() {
        return producer;
    }
}
