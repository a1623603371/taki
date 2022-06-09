package com.taki.common.message;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName TraceableMessage
 * @Description 可追溯消息
 * @Author Long
 * @Date 2022/6/9 12:39
 * @Version 1.0
 */
@Data
@NoArgsConstructor
public class TraceableMessage {

    /**\
     * trace id
     */
    private String traceId;

    /**
     * 消息体对应的类
     */
    private Class<?> calzz;

    /**
     * 消息类容
     */
    private String messageContent;

    public TraceableMessage(String traceId, Class<?> calzz, String messageContent) {
        this.traceId = traceId;
        this.calzz = calzz;
        this.messageContent = messageContent;
    }
}
