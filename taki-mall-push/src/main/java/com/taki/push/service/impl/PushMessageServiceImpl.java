package com.taki.push.service.impl;

import com.alibaba.fastjson.JSON;
import com.taki.common.constants.RocketMQConstant;
import com.taki.common.message.PlatformCouponUserBucketMessage;
import com.taki.common.message.PlatformMessagePushMessage;
import com.taki.common.utli.DateUtils;
import com.taki.common.utli.JsonUtil;
import com.taki.push.converter.ConditionConverter;
import com.taki.push.converter.MessagePushConverter;
import com.taki.push.dao.PushMessageCrontabDAO;
import com.taki.push.dao.PushMessageDAO;
import com.taki.push.domain.dto.*;
import com.taki.push.domain.entity.PushMessageCrontabDO;
import com.taki.push.domain.entity.PushMessageDO;
import com.taki.push.domain.request.QueryMessageRequest;
import com.taki.push.domain.request.SaveOrUpdateMessageRequest;
import com.taki.push.domain.request.SendMessageByAccountRequest;
import com.taki.push.enums.PushTypeEnum;
import com.taki.push.mq.producer.DefaultProducer;
import com.taki.push.service.MessageSendService;
import com.taki.push.service.PushMessageService;
import com.taki.user.domain.dto.MemberFilterDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @ClassName MessagePushServiceImpl
 * @Description 推送消息 service 组件
 * @Author Long
 * @Date 2022/10/7 19:54
 * @Version 1.0
 */
@Service
@Slf4j
public class PushMessageServiceImpl implements PushMessageService {

    @Autowired
    private PushMessageDAO pushMessageDAO;


    @Autowired
    private PushMessageCrontabDAO pushMessageCrontabDAO;


    @Autowired
    private MessagePushConverter messagePushConverter;


    @Autowired
    private ConditionConverter conditionConverter;


    @Autowired
    private DefaultProducer defaultProducer;




    @Transactional(rollbackFor = Exception.class)
    @Override
    public SaveOrUpdateMessageDTO saveOrUpdateMessage(SaveOrUpdateMessageRequest saveOrUpdateMessageRequest) {

        SaveOrUpdateMessageDTO saveOrUpdateMessageDTO = new SaveOrUpdateMessageDTO();

        //构造消息实体

        PushMessageDO pushMessageDO = buildPushMessageDO(saveOrUpdateMessageRequest);

        boolean saveFlag =pushMessageDAO.save(pushMessageDO);


        //定时发送消息
        if (Objects.equals(saveOrUpdateMessageRequest.getPushType(), PushTypeEnum.DELAY.getCode())){

            List<PushMessageCrontabDO> messageCrontabList = generateMessagePushMessageCrontab(saveOrUpdateMessageRequest);

            if (!CollectionUtils.isEmpty(messageCrontabList)){
                pushMessageCrontabDAO.saveBatch(messageCrontabList);
            }

            saveOrUpdateMessageDTO.setSuccess(saveFlag);

            return saveOrUpdateMessageDTO;
        }
        //构造推送消息DTO
        PushMessageDTO  pushMessageDTO = PushMessageDTO.builder()
                .mainMessage(saveOrUpdateMessageRequest.getMainMessage())
                .message(saveOrUpdateMessageRequest.getMessage())
                .messageType(saveOrUpdateMessageRequest.getInformType())
                .build();
        // 推送消息
        pushMessages(pushMessageDTO, saveOrUpdateMessageRequest.getMemberFilterDTO());

        saveOrUpdateMessageDTO.setSuccess(true);
        return saveOrUpdateMessageDTO;
    }
    
    /*** 
     * @description:  生成消息发送任务实体数据
     * @param saveOrUpdateMessageRequest 新增/修改 推送消息请求
     * @return
     * @author Long
     * @date: 2022/10/8 20:57
     */ 
    private List<PushMessageCrontabDO> generateMessagePushMessageCrontab(SaveOrUpdateMessageRequest saveOrUpdateMessageRequest) {

        Integer sendPeriodCount = saveOrUpdateMessageRequest.getSendPeriodCount();

        List<PushMessageCrontabDO> pushMessageCrontabList = new ArrayList<>();

        if (sendPeriodCount == 1){

            //周期内只推送一次，直接使用startTime作为定时任务
            PushMessageCrontabDO messageCrontab = buildMessagePushCrontabDO(saveOrUpdateMessageRequest,1,saveOrUpdateMessageRequest.getPushStartTime());
            pushMessageCrontabList.add(messageCrontab);

        }else {
            LocalDateTime startTime = saveOrUpdateMessageRequest.getPushStartTime();
            LocalDateTime endTime = saveOrUpdateMessageRequest.getPushEndTime();

            //开始时间和结束时间间隔分钟数
            Long minutes = DateUtils.betweenMinutes(startTime,endTime);

            long periodMinutes = minutes / (sendPeriodCount - 1);

            for (int i = 0; i < sendPeriodCount; i++){
                //任务执行时间计算逻辑
                // 从开始时间开始，作为第一次
                //后面每次间隔 periodMinutes 执行一次
                //最后一次使用结束时间
                PushMessageCrontabDO messageCrontabDO;
                if (i == sendPeriodCount){
                    messageCrontabDO = buildMessagePushCrontabDO(saveOrUpdateMessageRequest,i,saveOrUpdateMessageRequest.getPushEndTime());
                }else {
                    LocalDateTime crontabTime = startTime.plusMinutes(periodMinutes * (i - 1));

                    messageCrontabDO = buildMessagePushCrontabDO(saveOrUpdateMessageRequest,i,crontabTime);
                }
                pushMessageCrontabList.add(messageCrontabDO);
            }


        }

        return pushMessageCrontabList;
    }

    /*** 
     * @description: 构造消息发送任务实体
     * @param saveOrUpdateMessageRequest 新增/修改 推送消息请求
     * @param periodNumber 期数
     * @param pushStartTime 任务执行开始时间
     * @return  com.taki.push.domain.entity.PushMessageCrontabDO
     * @author Long
     * @date: 2022/10/9 21:36
     */ 
    private PushMessageCrontabDO buildMessagePushCrontabDO(SaveOrUpdateMessageRequest saveOrUpdateMessageRequest, int periodNumber, LocalDateTime pushStartTime) {

        PushMessageCrontabDO pushMessageCrontabDO = messagePushConverter.converterMessageCrontabDO(saveOrUpdateMessageRequest);

        pushMessageCrontabDO.setFilterCondition(JsonUtil.object2Json(saveOrUpdateMessageRequest.getMemberFilterDTO()));

        pushMessageCrontabDO.setCrontabTime(pushStartTime);

        pushMessageCrontabDO.setPeriodSendNumber(periodNumber);

        return pushMessageCrontabDO;

    }

    /*** 
     * @description: 构造 推送消息
     * @param saveOrUpdateMessageRequest 新增/修改 推送消息请求
     * @return  com.taki.push.domain.entity.PushMessageDO
     * @author Long
     * @date: 2022/10/8 20:57
     */ 
    private PushMessageDO buildPushMessageDO(SaveOrUpdateMessageRequest saveOrUpdateMessageRequest) {
        PushMessageDO pushMessageDO = messagePushConverter.converterMessageDO(saveOrUpdateMessageRequest);
        pushMessageDO.setFilterCondition(JsonUtil.object2Json(saveOrUpdateMessageRequest.getMemberFilterDTO()));

        return pushMessageDO;


    }

    @Override
    public List<QueryMessageDTO> queryMessageByCondition(QueryMessageRequest queryMessageRequest) {

        PushMessageDO  pushMessageDO = messagePushConverter.requestToEntity(queryMessageRequest);



        return messagePushConverter.listEntityToDTO(  pushMessageDAO.queryMessageByCondition(pushMessageDO));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public SendMessageDTO sendMessageByAccount(SendMessageByAccountRequest sendMessageByAccountRequest) {

        PushMessageDO pushMessageDO = messagePushConverter.converterMessageDO(sendMessageByAccountRequest);
        pushMessageDAO.save(pushMessageDO);

        PlatformMessagePushMessage platformMessagePushMessage = buildPlatformMessagePushMessage(sendMessageByAccountRequest);

        String msgJson = JsonUtil.object2Json(platformMessagePushMessage);

        defaultProducer.sendMessage(RocketMQConstant.PLATFORM_MESSAGE_SEND_TOPIC,msgJson,"平台消息推送消息");

        SendMessageDTO sendMessage = new SendMessageDTO();

        sendMessage.setSuccess(true);


        return sendMessage;
    }
    
    /*** 
     * @description:  构造 平台消息推送
     * @param sendMessageByAccountRequest 根据用户id 推送 消息 请求参数
     * @return  com.taki.common.message.PlatformMessagePushMessage
     * @author Long
     * @date: 2022/10/9 23:23
     */ 
    private PlatformMessagePushMessage buildPlatformMessagePushMessage(SendMessageByAccountRequest sendMessageByAccountRequest) {

        PlatformMessagePushMessage platformMessagePushMessage = PlatformMessagePushMessage.builder()
                .mainMessage(sendMessageByAccountRequest.getMainMessage())
                .message(sendMessageByAccountRequest.getMessage())
                .informType(sendMessageByAccountRequest.getInformType())
                .userId(Long.valueOf(sendMessageByAccountRequest.getUserId()))
                .build();

        return platformMessagePushMessage;

    }

    @Override
    public void pushMessages(PushMessageDTO pushMessageDTO, MemberFilterDTO memberFilterDTO) {

        //1.获取当前条件下count值
        PersonaFilterConditionDTO conditionDTO = conditionConverter.converterFilterCondition(memberFilterDTO);



    }
}

