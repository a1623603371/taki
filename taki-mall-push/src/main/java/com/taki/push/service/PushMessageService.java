package com.taki.push.service;

import com.taki.push.domain.dto.PushMessageDTO;
import com.taki.push.domain.dto.QueryMessageDTO;
import com.taki.push.domain.dto.SaveOrUpdateMessageDTO;
import com.taki.push.domain.dto.SendMessageDTO;
import com.taki.push.domain.request.QueryMessageRequest;
import com.taki.push.domain.request.SaveOrUpdateMessageRequest;
import com.taki.push.domain.request.SendMessageByAccountRequest;
import com.taki.user.domain.dto.MemberFilterDTO;

import java.util.List;

/**
 * @ClassName PushMessageService
 * @Description 推送 消息 service 组件
 * @Author Long
 * @Date 2022/10/6 21:55
 * @Version 1.0
 */
public interface PushMessageService {



    /*** 
     * @description:  新增/修改消息推送
     * @param saveOrUpdateMessageRequest
     * @return
     * @author Long
     * @date: 2022/10/7 19:55
     */ 
    SaveOrUpdateMessageDTO saveOrUpdateMessage(SaveOrUpdateMessageRequest saveOrUpdateMessageRequest);



    /*** 
     * @description:  查询消息记录
     * @param queryMessageRequest
     * @return  java.util.List<com.taki.push.domain.dto.QueryMessageDTO>
     * @author Long
     * @date: 2022/10/7 19:56
     */ 
    List<QueryMessageDTO> queryMessageByCondition(QueryMessageRequest queryMessageRequest);


    /*** 
     * @description: 根据 用户id 单点推送消息
     * @param sendMessageByAccountRequest
     * @return  com.taki.push.domain.dto.SendMessageDTO
     * @author Long
     * @date: 2022/10/7 20:14
     */ 
    SendMessageDTO sendMessageByAccount(SendMessageByAccountRequest sendMessageByAccountRequest);


    /*** 
     * @description:  筛选用户 推送消息
     * @param pushMessageDTO 推送消息
     * @param memberFilterDTO 过滤用户数据
     * @return  void
     * @author Long
     * @date: 2022/10/7 20:15
     */ 
    void pushMessages(PushMessageDTO  pushMessageDTO, MemberFilterDTO memberFilterDTO);
}
