package com.taki.push.service.impl;

import com.taki.common.message.PlatformMessagePushMessage;
import com.taki.push.domain.dto.EmailMessageSendDTO;
import com.taki.push.domain.dto.MessageSendDTO;
import com.taki.push.service.MessageSendService;
import com.taki.push.service.MessageSendServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName EmailSendServiceFactory
 * @Description TODO
 * @Author Long
 * @Date 2022/10/7 19:52
 * @Version 1.0
 */
public class EmailSendServiceFactory implements MessageSendServiceFactory {

    @Autowired
    private EmailSendServiceImpl emailSendService;

    @Override
    public MessageSendService createMessageSendService() {
        return emailSendService;
    }

    @Override
    public EmailMessageSendDTO createMessageSendDTO(PlatformMessagePushMessage platformMessagePushMessage) {

        EmailMessageSendDTO emailMessageSendDTO = new EmailMessageSendDTO();

        emailMessageSendDTO.setMainMessage(platformMessagePushMessage.getMainMessage());
        emailMessageSendDTO.setInformType(platformMessagePushMessage.getInformType());
        emailMessageSendDTO.setMessage(platformMessagePushMessage.getMessage());
        emailMessageSendDTO.setUserId(platformMessagePushMessage.getUserId());

        return emailMessageSendDTO;
    }
}
