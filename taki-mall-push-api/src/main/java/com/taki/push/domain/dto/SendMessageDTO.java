package com.taki.push.domain.dto;

import lombok.Data;

/**
 * @ClassName SendMessageDTO
 * @Description 消息推送结果
 * @Author Long
 * @Date 2022/10/5 16:11
 * @Version 1.0
 */
@Data
public class SendMessageDTO {

    /**
     * 是否发送成功
     */
    private Boolean success;
}
