package com.taki.push.service.impl;

import com.taki.common.message.PlatformMessagePushMessage;
import com.taki.push.domain.dto.AppMessageSendDTO;
import com.taki.push.domain.dto.MessageSendDTO;
import com.taki.push.service.MessageSendService;
import com.taki.push.service.MessageSendServiceFactory;
import org.springframework.stereotype.Component;

/**
 * @ClassName AppSendServiceFactory
 * @Description TODO
 * @Author Long
 * @Date 2022/10/6 22:28
 * @Version 1.0
 */
@Component
public class AppSendServiceFactory implements MessageSendServiceFactory {


    @Override
    public MessageSendService createMessageSendService() {
        return null;
    }

    @Override
    public MessageSendDTO createMessageSendDTO(PlatformMessagePushMessage platformMessagePushMessage) {

        AppMessageSendDTO messageSendDTO = new AppMessageSendDTO();

        messageSendDTO.setMainMessage(platformMessagePushMessage.getMainMessage());
        messageSendDTO.setMessage(platformMessagePushMessage.getMessage());
        messageSendDTO.setInformType(platformMessagePushMessage.getInformType());
        messageSendDTO.setUserId(platformMessagePushMessage.getUserId());

        return messageSendDTO;
    }
}
