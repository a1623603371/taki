package com.taki.order.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.message.ActualRefundMessage;
import com.taki.common.mq.AbstractMessageListenerConcurrently;
import com.taki.common.utlis.ResponseData;
import com.taki.order.domain.entity.AfterSaleRefundDO;
import com.taki.order.exception.OrderBizException;
import com.taki.order.service.OrderAfterSaleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName ActualRefundListener
 * @Description  MQ 实际退款 监听器
 * @Author Long
 * @Date 2022/2/18 9:51
 * @Version 1.0
 */
@Slf4j
@Component
public class ActualRefundListener  extends AbstractMessageListenerConcurrently {

    @Autowired
    private OrderAfterSaleService orderAfterSaleService;


    @Override
    protected ConsumeConcurrentlyStatus omMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            msgs.forEach(messageExt -> {
                String msg = new String(messageExt.getBody());
                ActualRefundMessage actualRefundMessage = JSONObject.parseObject(msg,ActualRefundMessage.class);
                log.info("实际退款 消费者  进行消费消息：{}",msg);

                ResponseData<Boolean> result = orderAfterSaleService.refundMoney(actualRefundMessage);

                if (!result.getSuccess()){
                    throw new OrderBizException(result.getCode(),result.getMessage());
                }
            });

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }catch (Exception e){
            log.error("consumer error",e);

            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }


    }
}
