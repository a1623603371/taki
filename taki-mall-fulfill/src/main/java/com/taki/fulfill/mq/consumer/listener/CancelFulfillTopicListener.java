package com.taki.fulfill.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.taki.fulfill.domain.request.CancelFulfillRequest;
import com.taki.fulfill.service.FulfillService;
import com.taki.tms.api.TmsApi;
import com.taki.wms.api.WmsApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName CancelFulfillTopicListener
 * @Description 取消 订单 履约 监听
 * @Author Long
 * @Date 2022/5/18 14:50
 * @Version 1.0
 */
@Slf4j
@Component
public class CancelFulfillTopicListener implements MessageListenerConcurrently {

    @Autowired
    private FulfillService fulfillService;


    @DubboReference(version = "1.0.0",retries = 0)
    private WmsApi wmsApi;

    @DubboReference(version = "1.0.0",retries = 0)
    private TmsApi tmsApi;


    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

        try {

            list.forEach(msg ->{
                String message = new String(msg.getBody());

                CancelFulfillRequest cancelFulfillRequest  = JSONObject.parseObject(message,CancelFulfillRequest.class);

                //取消 履约单
                fulfillService.cancelFulfillOrder(cancelFulfillRequest.getOrderId());

                //2 取消 拣货
                wmsApi.cancelPickGoods(cancelFulfillRequest.getOrderId());

                //3.取消发货

                tmsApi.cancelSendOut(cancelFulfillRequest.getOrderId());

            });




            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }catch (Exception e){
            log.error("取消订单履约信息失败",e);

            return ConsumeConcurrentlyStatus.RECONSUME_LATER;

        }

    }
}
