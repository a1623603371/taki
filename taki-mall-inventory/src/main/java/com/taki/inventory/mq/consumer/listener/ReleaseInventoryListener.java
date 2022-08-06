package com.taki.inventory.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.utli.ResponseData;
import com.taki.inventory.api.InventoryApi;
import com.taki.inventory.domain.request.ReleaseProductStockRequest;
import com.taki.inventory.exception.InventoryBizException;
import com.taki.inventory.exception.InventoryErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @ClassName ReleaseInventoryConsumer
 * @Description 监听释放库存消息
 * @Author Long
 * @Date 2022/2/17 14:09
 * @Version 1.0
 */
@Slf4j
@Component
public class ReleaseInventoryListener  implements MessageListenerConcurrently {

    @DubboReference(version = "1.0.0")
    private InventoryApi inventoryApi;


    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {
        list.forEach(msg->{
            String context = new String(msg.getBody(), StandardCharsets.UTF_8);

            log.info("ReleaseInventoryConsumer  message:{}",context);
            ReleaseProductStockRequest releaseProductStockRequest = JSONObject.parseObject(context,ReleaseProductStockRequest.class);
                ResponseData<Boolean> responseData =  inventoryApi.cancelOrderReleaseProductStock(releaseProductStockRequest);
            if (!responseData.getSuccess()){
                throw new InventoryBizException(InventoryErrorCodeEnum.CONSUME_MQ_FAILED);
            }
        });
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;

        }catch (Exception e){
            log.info("consumer  error",e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
    }
}
