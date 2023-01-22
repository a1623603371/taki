package com.taki.push.domain.request;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName SendMessageByAccountRequest
 * @Description 消息推送请求
 * @Author Long
 * @Date 2022/10/5 16:03
 * @Version 1.0
 */
@Data
@Builder
public class SendMessageByAccountRequest {

    private String userId;


    /**
     * 通知类型 1，短信 ，2 app消息 ，3 邮件
     */
    private Integer informType;


    /**
     * 消息摘要
     */
    private String mainMessage;


    /**
     * 消息
     */
    private String message;


}
