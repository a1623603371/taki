package com.taki.common.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName PlatformPromotionUserBucketMessage
 * @Description 平台促销活动用户桶消息
 * @Author Long
 * @Date 2022/10/3 22:10
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlatformPromotionUserBucketMessage implements Serializable {


    private static final long serialVersionUID = 5378201312884582415L;


    /**
     * 当前桶内起始的用户id（包含）
     */
    private Long startUserId;


    /**
     * 当前桶内结束的用户id（包含）
     */
    private Long endUserId;


    /**
     * 促销活动id
     */
    private Long promotionId;


    /**
     * 主题
     */
    private String mainMessage;


    /**
     * 消息内容
     */
    private String message;

    /**
     * 促销类型
     */
    private Integer promotionType;

    /**
     * 消息类型 1短信，2。app,3.email
     */
    private Integer informType;

    /**
     * 用户ID
     */
    private Long userAccountId;
}
