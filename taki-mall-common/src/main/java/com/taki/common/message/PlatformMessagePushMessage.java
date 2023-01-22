package com.taki.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName PlatformMessagePushMessage
 * @Description TODO
 * @Author Long
 * @Date 2022/10/6 23:12
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatformMessagePushMessage  implements Serializable {

    /**
     * 主题
     */
    private String mainMessage;


    /**
     * 消息内容
     */
    private String message;


    /**
     * 消息类型
     */
    private Integer informType;

    /**
     *  用户id
     */
    private Long userId;
}
