package com.taki.message.service.impl;

import com.taki.message.enums.SendTypeEnum;
import com.taki.message.enums.ShortMessagePlatformEnum;
import com.taki.message.service.SendMessageStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ShortMessageManage
 * @Description 短信发送管理组件
 * @Author Long
 * @Date 2021/12/6 16:20
 * @Version 1.0
 */
@Slf4j
@Component
public class ShortMessageManage {



    private final    TencentSDKStrategy tencentSDKStrategy;


    private final YunPianAPIStrategy yunPianAPIStrategy;


    private final TencentAPIStrategy tencentAPIStrategy;

    //
    private  Map<String, SendMessageStrategy> sendMessageStrategyMap = new HashMap<>();

    @Autowired
    public ShortMessageManage(TencentSDKStrategy tencentSDKStrategy, YunPianAPIStrategy yunPianAPIStrategy, TencentAPIStrategy tencentAPIStrategy) {
        this.tencentSDKStrategy = tencentSDKStrategy;
        this.yunPianAPIStrategy = yunPianAPIStrategy;
        this.tencentAPIStrategy = tencentAPIStrategy;

       // sendMessageStrategyMap.put(mergeKey(ShortMessagePlatformEnum.TX.name(),SendTypeEnum.SDK.name()),tencentSDKStrategy);
        sendMessageStrategyMap.put(mergeKey(ShortMessagePlatformEnum.TX.name(),SendTypeEnum.API.name()),tencentAPIStrategy);
        sendMessageStrategyMap.put(mergeKey(ShortMessagePlatformEnum.YP.name(),SendTypeEnum.API.name()),yunPianAPIStrategy);
    }

    /**
     * @description: 合并key
     * @param: shortMessagePlatformEnum
     * @param:  sendTypeEnum
     * @return: java.lang.String
     * @author Long
     * @date: 2021/12/6 16:59
     */
    private String mergeKey (String platformName, String sendType){
        return platformName +":" + sendType;
    }

    /**
     * 获取 发送策略
     * @param platformName 平台名称
     * @param sendType 发送方式
     * @return
     */
    public  SendMessageStrategy getSendMessageStrategy (String platformName,String sendType){
        return sendMessageStrategyMap.get(mergeKey(platformName,sendType));
    }



}
