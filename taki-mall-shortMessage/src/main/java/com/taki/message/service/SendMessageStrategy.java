package com.taki.message.service;

import com.taki.common.exception.ServiceException;
import com.taki.message.domian.dto.ShortMessagePlatformDTO;

/**
 * @ClassName SendMessageStrategy
 * @Description 发送短信策略 接口
 * @Author Long
 * @Date 2021/12/4 18:47
 * @Version 1.0
 */
public interface SendMessageStrategy {

    /**
     * @description:  发送短信
     * @param: phone 手机
     * @param: code 验证码
     *  @param: shortMessagePlatform 短信配置信息
     * @return: 是否发送成功
     * @author Long
     * @date: 2021/12/4 18:48
     */
    Boolean sendMessage(String areaCode,String phone, String code , ShortMessagePlatformDTO shortMessagePlatform) throws ServiceException;

}
