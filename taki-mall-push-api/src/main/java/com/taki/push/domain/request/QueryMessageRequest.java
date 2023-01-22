package com.taki.push.domain.request;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName QueryMessageRequest
 * @Description 查询消息请求
 * @Author Long
 * @Date 2022/10/5 15:51
 * @Version 1.0
 */
@Data
public class QueryMessageRequest {


    /**
     * 消息摘要（主题）
     */
    private String mainMessage;

    /**
     * 消息
     */
    private String message;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
