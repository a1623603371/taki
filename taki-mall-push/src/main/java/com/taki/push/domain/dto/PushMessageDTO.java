package com.taki.push.domain.dto;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Tolerate;

/**
 * @ClassName PushMessageDTO
 * @Description TODO
 * @Author Long
 * @Date 2022/10/6 15:11
 * @Version 1.0
 */
@Data
@Builder
public class PushMessageDTO {

    /**
     * 消息类型
     */
    private String mainMessage;

    /**
     * 消息
     */
    private String message;

    /**
     * 消息类型
     */
    private Integer messageType;


    @Tolerate
    public PushMessageDTO(){

    }
}
