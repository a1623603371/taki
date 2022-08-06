package com.taki.market.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.utli.ResponseData;
import com.taki.market.api.MarketApi;
import com.taki.market.exception.MarketBizException;
import com.taki.market.exception.MarketErrorCodeEnum;
import com.taki.market.request.CancelOrderReleaseUserCouponRequest;
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
 * @ClassName ReleasePropertyListener
 * @Description 释放资产 监听
 * @Author Long
 * @Date 2022/2/26 16:47
 * @Version 1.0
 */
@Slf4j
@Component
public class ReleasePropertyListener implements MessageListenerConcurrently {


    @DubboReference(version = "1.0.0")
    private MarketApi marketApi;

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
        try {

            list.forEach(msg ->{
            String context = new String(msg.getBody(), StandardCharsets.UTF_8);
            log.info("ReleasePropertyConsumer  message:{}",context);
            CancelOrderReleaseUserCouponRequest cancelOrderReleaseUserCouponRequest
                    = JSONObject.parseObject(context,CancelOrderReleaseUserCouponRequest.class);

                ResponseData<Boolean> responseData = marketApi.cancelOrderReleaseCoupon(cancelOrderReleaseUserCouponRequest);

                if (!responseData.getSuccess()){
                    throw new MarketBizException(MarketErrorCodeEnum.CONSUME_MQ_FAILED);
                }
            });
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }catch (Exception e){
        log.info("consumer error",e);

        return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }


    }
}
