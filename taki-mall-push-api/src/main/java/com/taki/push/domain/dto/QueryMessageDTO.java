package com.taki.push.domain.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @ClassName QueryMessageDTO
 * @Description 消息推送请求
 * @Author Long
 * @Date 2022/10/5 16:06
 * @Version 1.0
 */
@Data
public class QueryMessageDTO {


    /**
     * 消息类型 1.通知 ，2消息中心 ，3 即是通知也是消息中心
     * 通知类消息，借助第三方推送一个消息，不会保存数据库
     * 消息中心类消息，需要保存数据库
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
