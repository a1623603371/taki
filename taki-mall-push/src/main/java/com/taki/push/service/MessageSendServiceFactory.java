package com.taki.push.service;

import com.taki.common.message.PlatformCouponUserBucketMessage;
import com.taki.common.message.PlatformMessagePushMessage;
import com.taki.push.domain.dto.MessageSendDTO;

/**
 * @ClassName MessageSendServiceFactory
 * @Description TODO
 * @Author Long
 * @Date 2022/10/6 21:56
 * @Version 1.0
 */
public interface MessageSendServiceFactory  {

    /*** 
     * @description:  创建消息推送服务组件
     * @param 
     * @return  com.taki.push.service.MessageSendService
     * @author Long
     * @date: 2022/10/6 23:08
     */ 
    MessageSendService createMessageSendService();


    /*** 
     * @description:  创建消息推送DTO
     *  不同消息类型，可以构建不同消息推送DTO
     * @param
     * @return  com.taki.push.domain.dto.MessageSendDTO
     * @author Long
     * @date: 2022/10/6 23:09
     */ 
    MessageSendDTO createMessageSendDTO(PlatformMessagePushMessage platformMessagePushMessage);
}
