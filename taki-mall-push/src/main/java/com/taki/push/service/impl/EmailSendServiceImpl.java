package com.taki.push.service.impl;

import com.taki.common.message.PlatformMessagePushMessage;
import com.taki.push.domain.dto.EmailMessageSendDTO;
import com.taki.push.domain.dto.MessageSendDTO;
import com.taki.push.service.MessageSendService;
import com.taki.push.service.MessageSendServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName EmailSendServiceImpl
 * @Description 发送邮箱
 * @Author Long
 * @Date 2022/10/7 19:49
 * @Version 1.0
 */
@Slf4j
@Component
public class EmailSendServiceImpl implements MessageSendService<EmailMessageSendDTO> {

    @Override
    public Boolean send(EmailMessageSendDTO messagePushDTO) {

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }
}
