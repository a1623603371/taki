package com.taki.push.domain.dto;

import lombok.Data;

/**
 * @ClassName MessageSendDTO
 * @Description TODO
 * @Author Long
 * @Date 2022/10/6 15:15
 * @Version 1.0
 */
@Data
public class MessageSendDTO {

    /**
     *主题
     */
    private String mainMessage;

    /**
     * 消息
     */
    private String message;

    /**
     * 消息类型
     */
    private Integer informType;

    /**
     * 用户Id
     */
    private Long userId;

}
