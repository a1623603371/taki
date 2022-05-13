package com.taki.order.mq.consumer.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.constants.RocketMQConstant;
import com.taki.common.enums.OrderStatusEnum;
import com.taki.common.exception.ServiceException;
import com.taki.common.message.PaidOrderSuccessMessage;
import com.taki.common.redis.RedisLock;
import com.taki.fulfill.domain.request.ReceiveFulFillRequest;
import com.taki.order.dao.OrderInfoDao;
import com.taki.order.domain.entity.OrderInfoDO;
import com.taki.order.exception.OrderBizException;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.mq.producer.DefaultProducer;
import com.taki.order.service.OrderFulFillService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
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

    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private DefaultProducer defaultProducer;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            for (MessageExt messageExt : list) {
                String msg = new String(messageExt.getBody());
                PaidOrderSuccessMessage paidOrderSuccessMessage = JSONObject.parseObject(msg,PaidOrderSuccessMessage.class);

                String orderId = paidOrderSuccessMessage.getOrderId();
                log.info("触发订单履约 ，orderId :{}",orderId);


                OrderInfoDO orderInfoDO = orderInfoDao.getByOrderId(orderId);

                if (ObjectUtils.isEmpty(orderInfoDO)){
                    throw new OrderBizException(OrderErrorCodeEnum.ORDER_INFO_IS_NULL);
                }

                String key = RedisLockKeyConstants.ORDER_FULFILL_KEY + orderId;
                boolean lock =  redisLock.lock(key);

                if (!lock){
                    log.error("order has not  acquired lock cannot  fulfill  orderId ={}",orderId);
                    throw new OrderBizException(OrderErrorCodeEnum.ORDER_FULFILL_ERROR);
                }

                try {
                    //2. 进行订单履约逻辑
                    TransactionMQProducer producer = defaultProducer.getProducer();

                    producer.setTransactionListener(new TransactionListener() {
                        @Override
                        public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                            try {
                                orderFulFillService.triggerOrderFulfill(orderId);

                                return LocalTransactionState.COMMIT_MESSAGE;
                            } catch (ServiceException e) {
                                throw e;
                            } catch (Exception e) {
                                log.error("system error:{}", e);

                                return LocalTransactionState.ROLLBACK_MESSAGE;
                            }
                        }

                        @Override
                        public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {
                            // 检查 订单是否 “已履约” 状态
                            OrderInfoDO orderInfoDO = orderInfoDao.getByOrderId(orderId);

                            if (ObjectUtils.isNotEmpty(orderInfoDO) && orderInfoDO.getOrderStatus().equals(OrderStatusEnum.FULFILL.getCode())) {

                                return LocalTransactionState.COMMIT_MESSAGE;
                            }

                            return LocalTransactionState.ROLLBACK_MESSAGE;
                        }
                    });
                    ReceiveFulFillRequest receiveFulFillRequest = orderFulFillService.builderReceiveFulFillRequest(orderInfoDO);


                    String topic = RocketMQConstant.TRIGGER_ORDER_FULFILL_TOPIC;

                    byte[] body = JSON.toJSONString(receiveFulFillRequest).getBytes(StandardCharsets.UTF_8);

                    Message message = new Message(topic,body);

                    producer.sendMessageInTransaction(message,orderInfoDO);


               }finally {
                    if (lock){
                        redisLock.unLock(key);
                    }
                }
            }

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }catch (Exception e){
            log.error("consumer  error",e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }

    }
}
