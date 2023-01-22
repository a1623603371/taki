package com.taki.push.service.impl;

import com.taki.push.domain.dto.AppMessageSendDTO;
import com.taki.push.service.MessageSendService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName AppSendServiceImpl
 * @Description TODO
 * @Author Long
 * @Date 2022/10/6 22:29
 * @Version 1.0
 */
@Slf4j
@Component
public class AppSendServiceImpl implements MessageSendService<AppMessageSendDTO> {
    @Override
    public Boolean send(AppMessageSendDTO messagePushDTO) {
        log.info("app消息推送中：{}",messagePushDTO);
        try {

            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return true;
    }
}
