package com.taki.push.domain.dto;

import lombok.Data;

/**
 * @ClassName SmsMessageSendDTO
 * @Description TODO
 * @Author Long
 * @Date 2022/10/6 15:22
 * @Version 1.0
 */
@Data
public class SmsMessageSendDTO extends MessageSendDTO {

    /**
     * 手机号
     */
    private String phoneNum;
}
