package com.taki.message.service.impl;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonObject;
import com.taki.core.error.ServiceException;
import com.taki.core.utlis.HttpClientUtils;
import com.taki.message.domian.dto.ShortMessagePlatformDTO;
import com.taki.message.domian.dto.YunPianReuqst;
import com.taki.message.service.SendMessageStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import springfox.documentation.spring.web.json.Json;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName YunPianAPIStrategy
 * @Description 云片API 发送短信
 * @Author Long
 * @Date 2021/12/6 11:15
 * @Version 1.0
 */
@Slf4j
@Component
public class YunPianAPIStrategy implements SendMessageStrategy {

    private static final String codeStatus = "0";

    @Override
    public Boolean sendMessage(String phone, String code, ShortMessagePlatformDTO shortMessagePlatform) throws ServiceException {
        // 构建短信请求数据
        YunPianReuqst reuqst = YunPianReuqst.builder()
                .apikey(shortMessagePlatform.getApiKey())
                .mobile(phone)
                .text("[] " + code)
                .build();
           String response = HttpClientUtils.requestPostJson(null,8000,shortMessagePlatform.getRequestUrl(),null, JSON.toJSONString(reuqst));

        String responseCode = JSON.parseObject(response).getString("code");
        if (!codeStatus.equals(responseCode)){
            return false;
        }
        return true;
    }





}
