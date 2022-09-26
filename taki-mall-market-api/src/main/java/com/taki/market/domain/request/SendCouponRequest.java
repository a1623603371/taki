package com.taki.market.domain.request;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName SendCouponRequest
 * @Description 领取优惠券请求
 * @Author Long
 * @Date 2022/9/26 21:32
 * @Version 1.0
 */
@Data
@Builder
public class SendCouponRequest {

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 优惠券类型
     */
    private Integer couponType;


    /**
     * 优惠券规则
     */
    private String couponRule;

    /**
     *活动开始时间
     */
    private LocalDateTime activityStartTime;

    /**
     * 活动结束时间
     */
    private LocalDateTime activityEndTime;

    /**
     *优惠券发放数量
     */
    private Integer couponCount;

    /**
     * 优惠券领取数量
     */
    private Integer couponReceivedCount;

    /**
     * 优惠券领取地址连接
     */
    private String activeUrl;

    /**
     * 推送类型 1-定时推送 ， 2.实时发送
     */
    private Integer pushType;

    /**
     * 1.短信 2.app消息 3.邮箱
     */
    private Integer informType;

    /**
     *定时发送消息任务开始时间
     */
    private LocalDateTime pushStartTime;

    /**
     * 定时任务发送消息任务结束时间
     */
    private LocalDateTime pushEndTime;

    /**
     * 每个发送周期内的发送次数，以此为依据发送消息
     */
    private Integer sendPeriodCount;

    /**
     * 创建用户
     */
    private String createUser;
}
