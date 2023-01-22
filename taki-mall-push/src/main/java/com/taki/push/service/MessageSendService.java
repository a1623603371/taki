package com.taki.push.service;

import com.taki.push.domain.dto.MessageSendDTO;
import org.springframework.stereotype.Service;

/**
 * @ClassName PushMessageCrontabService
 * @Description TODO
 * @Author Long
 * @Date 2022/10/6 21:55
 * @Version 1.0
 */

public interface MessageSendService<T extends MessageSendDTO> {

    /*** 
     * @description:  消息发送
     * @param messagePushDTO
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/10/9 23:12
     */ 
    Boolean send(T messagePushDTO);
}
