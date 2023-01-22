package com.taki.push.api;

import com.taki.common.utli.ResponseData;
import com.taki.push.domain.dto.QueryMessageDTO;
import com.taki.push.domain.dto.SaveOrUpdateMessageDTO;
import com.taki.push.domain.dto.SendMessageDTO;
import com.taki.push.domain.request.QueryMessageRequest;
import com.taki.push.domain.request.SaveOrUpdateMessageRequest;
import com.taki.push.domain.request.SendMessageByAccountRequest;

import java.util.List;

/**
 * @ClassName MessageApi
 * @Description 消息推送接口
 * @Author Long
 * @Date 2022/10/5 16:12
 * @Version 1.0
 */
public interface MessagePushApi {



    /*** 
     * @description:  新增/保存消息推送
     * @param saveOrUpdateMessageRequest
     * @return  com.taki.common.utli.ResponseData<com.taki.push.domain.dto.SaveOrUpdateMessageDTO>
     * @author Long
     * @date: 2022/10/5 16:13
     */ 
    ResponseData<SaveOrUpdateMessageDTO> saveOrUpdateMessage(SaveOrUpdateMessageRequest saveOrUpdateMessageRequest);



    /*** 
     * @description:  根据userId 单点推送消息
     * @param sendMessageByAccountRequest 消息推送请求
     * @return
     * @author Long
     * @date: 2022/10/5 16:18
     */ 
    ResponseData<SendMessageDTO> sendMessageByAccount(SendMessageByAccountRequest sendMessageByAccountRequest);


    /*** 
     * @description:  查询消息记录
     * @param queryMessageRequest  查询消息请求
     * @return
     * @author Long
     * @date: 2022/10/5 16:19
     */ 
    ResponseData<List<QueryMessageDTO>> queryMessageByCondition(QueryMessageRequest queryMessageRequest);
}
