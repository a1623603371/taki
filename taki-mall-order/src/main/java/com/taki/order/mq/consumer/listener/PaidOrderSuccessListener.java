package com.taki.order.mq.consumer.listener;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.constants.RocketMQConstant;
import com.taki.common.enums.OrderStatusEnum;
import com.taki.common.exception.ServiceException;
import com.taki.common.message.PaidOrderSuccessMessage;
import com.taki.common.mq.AbstractMessageListenerConcurrently;
import com.taki.common.mq.MQMessage;
import com.taki.common.redis.RedisLock;

import com.taki.fulfill.domain.request.ReceiveFulfillRequest;
import com.taki.order.dao.OrderInfoDao;
import com.taki.order.domain.entity.OrderInfoDO;
import com.taki.order.exception.OrderBizException;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.mq.producer.DefaultProducer;
import com.taki.order.mq.producer.TriggerOrderFulfillProducer;
import com.taki.order.service.OrderFulFillService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.*;
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
public class PaidOrderSuccessListener extends AbstractMessageListenerConcurrently {

    @Autowired
    private OrderFulFillService orderFulFillService;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private TriggerOrderFulfillProducer triggerOrderFulfillProducer;



    @Override
    protected ConsumeConcurrentlyStatus omMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            for (MessageExt messageExt : msgs) {
                String msg = new String(messageExt.getBody());
                PaidOrderSuccessMessage paidOrderSuccessMessage = JSONObject.parseObject(msg,PaidOrderSuccessMessage.class);

                String orderId = paidOrderSuccessMessage.getOrderId();
                log.info("触发订单履约 ，orderId :{}",orderId);


                OrderInfoDO orderInfoDO = orderInfoDao.getByOrderId(orderId);

                if (ObjectUtils.isEmpty(orderInfoDO)){
                    throw new OrderBizException(OrderErrorCodeEnum.ORDER_INFO_IS_NULL);
                }

                String key = RedisLockKeyConstants.ORDER_FULFILL_KEY + orderId;
                boolean lock =  redisLock.tryLock(key);

                if (!lock){
                    log.error("order has not  acquired lock cannot  fulfill  orderId ={}",orderId);
                    throw new OrderBizException(OrderErrorCodeEnum.ORDER_FULFILL_ERROR);
                }

                try {
                    //2. 进行订单履约逻辑
                    Boolean result =  triggerOrderFulfill(orderId,orderInfoDO);
                    if (!result){
                        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
                    }
                }finally {
                    if (lock){
                        redisLock.unLock(key);
                    }
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }catch (Exception e){
            log.error("consumer  error",e);
            // 执行失败 触发重新消费
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
    
    /** 
     * @description: 通知履约系统触发订单进行履约
     * @param orderId 订单Id
     * @param  orderInfoDO 订单信息
     * @return
     * @author Long
     * @date: 2022/6/9 18:34
     */ 
    private Boolean triggerOrderFulfill(String orderId, OrderInfoDO orderInfoDO) throws MQClientException {

        TransactionMQProducer transactionMQProducer = triggerOrderFulfillProducer.getTransactionMQProducer();

        setTriggerOrderFulfillTransactionListener(transactionMQProducer);

        ReceiveFulfillRequest receiveFulFillRequest = orderFulFillService.builderReceiveFulFillRequest(orderInfoDO);

        Boolean result =  sendTriggerOrderFulfillSuccessMessage(transactionMQProducer,receiveFulFillRequest,orderId,orderInfoDO);

        return result;
    }

    /**
     * @description: 发送 通知履约系统触发订单进行履约 消息
     * @param transactionMQProducer
     * @param receiveFulFillRequest
     * @return  void
     * @author Long
     * @date: 2022/6/9 18:42
     */
    private Boolean sendTriggerOrderFulfillSuccessMessage(TransactionMQProducer transactionMQProducer,
                                                          ReceiveFulfillRequest receiveFulFillRequest,
                                                          String orderId, OrderInfoDO orderInfoDO) throws MQClientException {

        String topic = RocketMQConstant.TRIGGER_ORDER_FULFILL_TOPIC;

        byte[] body = JSON.toJSONString(receiveFulFillRequest).getBytes(StandardCharsets.UTF_8);

        Message message = new MQMessage(topic,null,orderId,body);

        TransactionSendResult result =  transactionMQProducer.sendMessageInTransaction(message,orderInfoDO);
        return SendStatus.SEND_OK.equals(result.getSendStatus());
    }

    /*** 
     * @description: 设置 履约事务消息 回调监听器
     * @param transactionMQProducer
     * @return  void
     * @author Long
     * @date: 2022/6/9 18:39
     */ 
    private void setTriggerOrderFulfillTransactionListener(TransactionMQProducer transactionMQProducer) {

        transactionMQProducer.setTransactionListener(new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message message, Object o) {
                try {
                    OrderInfoDO orderInfoDO = (OrderInfoDO) o;

                    orderFulFillService.triggerOrderFulfill(orderInfoDO.getOrderId());

                    return LocalTransactionState.COMMIT_MESSAGE;
                } catch (ServiceException e) {
                    log.error("biz  error:{}", e);
                    return LocalTransactionState.ROLLBACK_MESSAGE;

                } catch (Exception e) {
                    log.error("system error:{}", e);

                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt messageExt) {

                ReceiveFulfillRequest receiveFulfillRequest = JSON.parseObject(
                        new String(messageExt.getBody(), StandardCharsets.UTF_8), ReceiveFulfillRequest.class);
                // 检查 订单是否 “已履约” 状态
                OrderInfoDO orderInfoDO = orderInfoDao.getByOrderId(receiveFulfillRequest.getOrderId());

                if (ObjectUtils.isNotEmpty(orderInfoDO) && orderInfoDO.getOrderStatus().equals(OrderStatusEnum.FULFILL.getCode())) {

                    return LocalTransactionState.COMMIT_MESSAGE;
                }
                return LocalTransactionState.ROLLBACK_MESSAGE;
            }
        });
    }
}
