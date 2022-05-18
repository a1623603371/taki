package com.taki.fulfill.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.taki.fulfill.domain.request.ReceiveFulFillRequest;
import com.taki.fulfill.service.FulfillService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName TriggerOrderFulfillTopicListener
 * @Description 接受 订单 履约请求
 * @Author Long
 * @Date 2022/5/18 14:56
 * @Version 1.0
 */
@Component
@Slf4j
public class TriggerOrderFulfillTopicListener implements MessageListenerConcurrently {


    @Autowired
    private FulfillService fulfillService;


    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

        try {
            list.forEach(messageExt -> {
                String message = new String(messageExt.getBody());

                ReceiveFulFillRequest request = JSONObject.parseObject(message,ReceiveFulFillRequest.class);
                fulfillService.receiveOrderFulFill(request);
            });




            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }catch (Exception e){
            log.error("接受订单履约 error",e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }

    }
}
