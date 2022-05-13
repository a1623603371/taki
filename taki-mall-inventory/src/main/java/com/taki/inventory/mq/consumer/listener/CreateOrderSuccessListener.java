package com.taki.inventory.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.taki.inventory.domain.request.DeductProductStockRequest;
import com.taki.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName CreateOrderSuccessListener
 * @Description 创建订单成功后监听消息
 * @Author Long
 * @Date 2022/5/13 18:23
 * @Version 1.0
 */
@Component
@Slf4j
public class CreateOrderSuccessListener implements MessageListenerConcurrently {

    @Autowired
    private InventoryService inventoryService;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            list.forEach(msg ->{
                String message = new String(msg.getBody());

                DeductProductStockRequest deductProductStockRequest = JSONObject.parseObject(message,DeductProductStockRequest.class);
                // 触发 扣减库存
                inventoryService.deductProductStock(deductProductStockRequest);

            });

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }catch (Exception e){
            log.error("system error:{}",e);
            // 本地业务逻辑执行失败，触发消息重新消费
            return  ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }

    }
}
