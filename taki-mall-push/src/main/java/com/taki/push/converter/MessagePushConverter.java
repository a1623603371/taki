package com.taki.push.converter;

import com.taki.push.domain.dto.QueryMessageDTO;
import com.taki.push.domain.entity.PushMessageCrontabDO;
import com.taki.push.domain.entity.PushMessageDO;
import com.taki.push.domain.request.QueryMessageRequest;
import com.taki.push.domain.request.SaveOrUpdateMessageRequest;
import com.taki.push.domain.request.SendMessageByAccountRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * @ClassName MessagePushConverter
 * @Description TODO
 * @Author Long
 * @Date 2022/10/5 21:28
 * @Version 1.0
 */
@Mapper(componentModel = "spring")
public interface MessagePushConverter {

    
    /*** 
     * @description:  转换对象
     * @param request
     * @return
     * @author Long
     * @date: 2022/10/6 17:57
     */ 
    @Mapping(target = "messageInfo",source = "message")
    PushMessageDO requestToEntity(QueryMessageRequest  request);


    @Mapping(target = "message",source = "messageInfo")
    QueryMessageDTO entityToDTO(PushMessageDO pushMessageDO);



    List<QueryMessageDTO> listEntityToDTO(List<PushMessageDO> pushMessages);


    @Mapping(target = "messageInfo",source = "message")
    PushMessageDO converterMessageDO(SaveOrUpdateMessageRequest  request);

    @Mapping(target = "messageInfo",source = "message")
    PushMessageDO converterMessageDO(SendMessageByAccountRequest request);

    @Mapping(target = "messageInfo",source = "message")
    PushMessageCrontabDO converterMessageCrontabDO(SaveOrUpdateMessageRequest request);
}
