package com.taki.customer.mq.consumer.listener;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.mq.AbstractMessageListenerConcurrently;
import com.taki.customer.domain.request.CustomReviewReturnGoodsRequest;
import com.taki.customer.exception.CustomerBizException;
import com.taki.customer.exception.CustomerErrorCodeEnum;
import com.taki.customer.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName AfterSaleCustomerAuditTopicListener
 * @Description 接受订单系统售后审核申请
 * @Author Long
 * @Date 2022/7/31 14:29
 * @Version 1.0
 */
@Slf4j
@Component
public class AfterSaleCustomerAuditTopicListener extends AbstractMessageListenerConcurrently {

    @Autowired
    private CustomerService customerService;


    @Override
    protected ConsumeConcurrentlyStatus omMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

        try {
            msgs.forEach(messageExt -> {
                String message = new String(messageExt.getBody());
                log.info("AfterSaleCustomerAuditTopicListener message:{}",messageExt);
                CustomReviewReturnGoodsRequest customReviewReturnGoodsRequest = JSONObject.parseObject(message,CustomReviewReturnGoodsRequest.class);

                //客服接受订单系统的售后申请
                Boolean result =   customerService.customerAudit(customReviewReturnGoodsRequest);

                if (!result){
                    throw new CustomerBizException(CustomerErrorCodeEnum.PROCESS_RECEIVE_AFTER_SALE);

                }

            });
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        }catch (Exception e){
            log.error("consumer error",e);
            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }

    }
}
