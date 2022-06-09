package com.taki.order.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.constants.RocketMQConstant;
import com.taki.common.message.ActualRefundMessage;
import com.taki.common.mq.AbstractMessageListenerConcurrently;
import com.taki.inventory.domain.request.ReleaseProductStockRequest;
import com.taki.order.domain.dto.ReleaseProductStockDTO;
import com.taki.order.domain.request.AuditPassReleaseAssetsRequest;
import com.taki.order.mq.producer.DefaultProducer;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName AuditPassReleaseAssetsListens
 * @Description 接收客服审核通过后 的 监听 释放资产消息
 * @Author Long
 * @Date 2022/5/13 15:37
 * @Version 1.0
 */
@Slf4j
@Component
public class AuditPassReleaseAssetsListens extends AbstractMessageListenerConcurrently {


    @Autowired
    private DefaultProducer defaultProducer;



    @Override
    protected ConsumeConcurrentlyStatus omMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
            msgs.forEach(msg->{
                // 1.消费者 释放资产 message
                String message = new String(msg.getBody());
                log.info("auditPassReleaseAssetsListen message:{}",message);
                AuditPassReleaseAssetsRequest auditPassReleaseAssetRequest = JSONObject.parseObject(message,AuditPassReleaseAssetsRequest.class);

                //2.发送释放库存MQ
                ReleaseProductStockDTO releaseProductStockDTO = auditPassReleaseAssetRequest.getReleaseProductStockDTO();

                ReleaseProductStockRequest releaseProductStockRequest = buildReleaseProductStock(releaseProductStockDTO);

                defaultProducer.sendMessage(RocketMQConstant.CANCEL_RELEASE_INVENTORY_TOPIC,
                        JSONObject.toJSONString(releaseProductStockRequest),"客服审核通过释放库存",null,null);

                //3 。发送实际退款

                ActualRefundMessage actualRefundMessage = auditPassReleaseAssetRequest.getActualRefundMessage();
                defaultProducer.sendMessage(RocketMQConstant.ACTUAL_REFUND_TOPIC,
                        JSONObject.toJSONString(actualRefundMessage),"客服审核通过实际退款",null,null);

            });

            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }catch (Exception e){
            log.error("consumer error");
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;

        }
    }

    /** 
     * @description: 组装释放库存数据
     * @param releaseProductStockDTO 释放商品库存 数据
     * @return  释放商品库存请求
     * @author Long
     * @date: 2022/5/13 17:01
     */
    private ReleaseProductStockRequest buildReleaseProductStock(ReleaseProductStockDTO releaseProductStockDTO) {
        List<ReleaseProductStockRequest.OrderItemRequest> orderItemRequests = new ArrayList<>();

        // 补充条目
        releaseProductStockDTO.getOrderItemRequests().forEach(orderItemRequest -> {
            ReleaseProductStockRequest.OrderItemRequest myOrderItemRequest = orderItemRequest.clone(ReleaseProductStockRequest.OrderItemRequest.class);
            orderItemRequests.add(myOrderItemRequest);
        });
        ReleaseProductStockRequest releaseProductStockRequest = new ReleaseProductStockRequest();
        releaseProductStockRequest.setOrderId(releaseProductStockDTO.getOrderId());
        releaseProductStockRequest.setOrderItemRequests(orderItemRequests);

        return releaseProductStockRequest;


    }
}
