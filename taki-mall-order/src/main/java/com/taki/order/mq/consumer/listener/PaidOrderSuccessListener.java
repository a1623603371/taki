package com.taki.order.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.message.PaidOrderSuccessMessage;
import com.taki.common.redis.RedisLock;
import com.taki.order.exception.OrderBizException;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.service.OrderFulFillService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName PaidOrderSuccessListener
 * @Description 监听支付成功后的消息
 * @Author Long
 * @Date 2022/4/6 16:11
 * @Version 1.0
 */
@Component
@Slf4j
public class PaidOrderSuccessListener implements MessageListenerConcurrently {

    @Autowired
    private OrderFulFillService orderFulFillService;

    @Autowired
    private RedisLock redisLock;



    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            for (MessageExt messageExt : list) {
                String msg = new String(messageExt.getBody());
                PaidOrderSuccessMessage paidOrderSuccessMessage = JSONObject.parseObject(msg,PaidOrderSuccessMessage.class);

                String orderId = paidOrderSuccessMessage.getOrderId();
                log.info("触发订单履约 ，orderId :{}",orderId);

                String key = RedisLockKeyConstants.ORDER_FULFILL_KEY + orderId;
                boolean lock =  redisLock.lock(key);

                if (!lock){
                    log.error("order has not  acquired lock cannot  fulfill  orderId ={}",orderId);
                    throw new OrderBizException(OrderErrorCodeEnum.ORDER_FULFILL_ERROR);
                }

                try {
                    orderFulFillService.triggerOrderFulfill(orderId);

               }finally {
                    if (lock){
                        redisLock.unLock(key);
                    }
                }
            }

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }catch (Exception e){
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }

    }
}
