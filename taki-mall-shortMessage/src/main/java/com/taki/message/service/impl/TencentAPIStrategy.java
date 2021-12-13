package com.taki.message.service.impl;

import com.taki.message.domian.dto.ShortMessagePlatformDTO;
import com.taki.message.service.SendMessageStrategy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName TencentAPIStrategy
 * @Description 腾讯 发送 短信 API 发送发送
 * @Author Long
 * @Date 2021/12/6 10:52
 * @Version 1.0
 */
@Slf4j
@Component
public class TencentAPIStrategy implements SendMessageStrategy {






    @Override
    public Boolean sendMessage(String areaCode,String phone, String code, ShortMessagePlatformDTO shortMessagePlatform) {

        try {

        }catch (Exception e){
            log.error(e.getLocalizedMessage());
            e.printStackTrace();
        }




        return null;
    }
}
