package com.taki.order.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.core.BeanCopierUtils;
import com.taki.common.enums.OrderStatusChangEnum;
import com.taki.common.redis.RedisLock;
import com.taki.fulfill.domain.evnet.OrderDeliveredWmsEvent;
import com.taki.fulfill.domain.evnet.OrderEvent;
import com.taki.fulfill.domain.evnet.OrderOutStockWmsEvent;
import com.taki.order.domain.dto.WmsShipDTO;
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
 * @ClassName OrderWmsShipResultListener
 * @Description TODO
 * @Author Long
 * @Date 2022/4/6 14:24
 * @Version 1.0
 */
@Component
@Slf4j
public class OrderWmsShipResultListener implements MessageListenerConcurrently {

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private OrderFulFillService orderFulFillService;


    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

        OrderEvent orderEvent;

        try {
            for (MessageExt msg : list) {

            String message = new String(msg.getBody());
            log.info("received orderWmsShopResult  message:{}",msg);
            orderEvent = JSONObject.parseObject(message,OrderEvent.class);
            // 1.解析信息
            WmsShipDTO wmsShipDTO = buildWmsShip(orderEvent);

            // 2.加分布式锁  防止前置状态 效验防止 消息重复消费
            String key = RedisLockKeyConstants.ORDER_WMS_RESULT_KEY + wmsShipDTO.getOrderId();
            boolean lock = redisLock.lock(key);
            if (!lock){
                log.error(" order  has  not  acquired lock, cannot inform order  wms result  orderId={}",wmsShipDTO.getOrderId());

                throw new OrderBizException(OrderErrorCodeEnum.ORDER_NOT_ALLOW_INFORM_WMS_RESULT);
            }

            // 3.通知 订单物流配送
            //
                try {
                    orderFulFillService.informOrderWmsShipResult(wmsShipDTO);
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


    /**
     * @description: 构造物流配送结果请求
     * @param orderEvent 订单事件
     * @return
     * @author Long
     * @date: 2022/4/6 14:39
     */
    public  WmsShipDTO buildWmsShip(OrderEvent orderEvent) {
        String messageContent = JSONObject.toJSONString(orderEvent.getMessageContent());

        WmsShipDTO wmsShipDTO = new WmsShipDTO();
        wmsShipDTO.setStatusChang(orderEvent.getOrderStatusChang());
        if (OrderStatusChangEnum.ORDER_OUT_STOCKED.equals(orderEvent.getOrderStatusChang())){
            //订单已出库单
            OrderOutStockWmsEvent orderOutStockWmsEvent = JSONObject.parseObject(messageContent,OrderOutStockWmsEvent.class);
            BeanCopierUtils.copyProperties(orderOutStockWmsEvent,wmsShipDTO);
        }else if (OrderStatusChangEnum.ORDER_DELIVERED.equals(orderEvent.getOrderStatusChang())){
            // 订单已配送信息
            OrderDeliveredWmsEvent orderDeliveredWmsEvent = JSONObject.parseObject(messageContent,OrderDeliveredWmsEvent.class);
            BeanCopierUtils.copyProperties(orderDeliveredWmsEvent,wmsShipDTO);

        }

        return wmsShipDTO;
    }
}
