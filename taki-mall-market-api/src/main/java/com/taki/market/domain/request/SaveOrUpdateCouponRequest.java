package com.taki.market.domain.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName SaveOrUpdateCouponRequest
 * @Description 创建/修改优惠券活动请求
 * @Author Long
 * @Date 2022/10/3 23:51
 * @Version 1.0
 */
@Data
@Builder
public class SaveOrUpdateCouponRequest implements Serializable {

    /**
     * 优惠券名称
     */
    private String couponName;

    /**
     * 优惠券规则
     */
    private  String couponRule;

    /**
     * 活动开始时间
     */
    private LocalDateTime activityStartTime;

    /**
     * 活动结束时间
     */
    private LocalDateTime activityEndTime;

    /**
     * 1：短信 2.app消息 3. 邮箱
     */
    private Integer informType;

    /**
     * 优惠券发送数量
     */
    private Integer couponCount;

    /**
     *优惠券领取数量
     */
    private Integer couponReceiveCount;

    /**
     * 优惠券发放方式 1.仅自己领取 2.系统发放
     */
    private Integer couponReceiveType;

    /**
     * 优惠券类型
     */
    private Integer couponType;

    /**
     * 优惠券状态 1.发放中，2 已发完，3，已过期
     */
    private Integer couponStatus;


    /**
     * 活动创建人/修改人
     */
    private Integer createUser;
}
