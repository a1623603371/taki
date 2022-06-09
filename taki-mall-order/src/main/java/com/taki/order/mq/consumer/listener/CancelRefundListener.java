package com.taki.order.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.mq.AbstractMessageListenerConcurrently;
import com.taki.common.utlis.ResponseData;
import com.taki.order.domain.request.CancelOrderAssembleRequest;
import com.taki.order.exception.OrderBizException;
import com.taki.order.exception.OrderErrorCodeEnum;
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
 * @ClassName CancelRefundListener
 * @Description 取消 退款 监听器
 * @Author Long
 * @Date 2022/4/5 18:42
 * @Version 1.0
 */
@Component
@Slf4j
public class CancelRefundListener extends AbstractMessageListenerConcurrently {

    @Autowired
    private OrderAfterSaleService orderAfterSaleService;



    @Override
    protected ConsumeConcurrentlyStatus omMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            msgs.forEach(messageExt -> {
                String msg = new String(messageExt.getBody());
                CancelOrderAssembleRequest cancelOrderAssembleRequest = JSONObject.parseObject(msg,CancelOrderAssembleRequest.class);
                log.info("CancelRefundConsumer  message:{}",cancelOrderAssembleRequest);

                // 执行 取消订单/超时 未支付 取消 前 的操作

                ResponseData<Boolean> result = orderAfterSaleService.processCancelOrder(cancelOrderAssembleRequest);
                if (!result.getSuccess()){
                    throw new OrderBizException(OrderErrorCodeEnum.CONSUME_MQ_FAILED);
                }
            });

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }catch (Exception e){
            log.error("consumer  error",e);

            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}
