package com.taki.market.service;

import com.taki.market.domain.dto.UserCouponDTO;
import com.taki.market.request.LockUserCouponRequest;
import com.taki.market.request.ReleaseUserCouponRequest;
import com.taki.market.request.UserCouponQuery;

/**
 * @ClassName CouponService
 * @Description 优惠券 service 组件
 * @Author Long
 * @Date 2022/2/18 11:52
 * @Version 1.0
 */
public interface CouponService {

    /***
     * @description: 获取用户优惠券
     * @param userCouponQuery 用户优惠券查询条件
     * @return  用户优惠券信息
     * @author Long
     * @date: 2022/2/18 11:53
     */
    UserCouponDTO  getUserCoupon(UserCouponQuery userCouponQuery);


    /***
     * @description: 锁定用户优惠券
     * @param lockUserCouponRequest 锁定用户优惠券 请求
     * @return  处理结果
     * @author Long
     * @date: 2022/2/18 11:59
     */
    Boolean lockUserCoupon(LockUserCouponRequest lockUserCouponRequest);


    /**
     * @description: 释放 已使用的用户优惠券
     * @param releaseUserCouponRequest 释放 已使用的用户优惠券 请求
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/2/18 14:17
     */
    Boolean releaseUserCoupon(ReleaseUserCouponRequest releaseUserCouponRequest);



}
