package com.taki.message.service;

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
     * @return: java.lang.Boolean
     * @author Long
     * @date: 2021/12/4 18:48
     */
    Boolean sendMessage(String phone,String code);


}
