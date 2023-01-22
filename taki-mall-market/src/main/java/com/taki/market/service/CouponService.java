package com.taki.market.service;

import com.taki.market.domain.dto.ReceiveCouponDTO;
import com.taki.market.domain.dto.SaveOrUpdateCouponDTO;
import com.taki.market.domain.dto.SendCouponDTO;
import com.taki.market.domain.dto.UserCouponDTO;
import com.taki.market.domain.request.*;
import com.taki.market.domain.query.UserCouponQuery;

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


    /**
     * @description: 保存/修改 优惠券
     * @param saveOrUpdateCouponRequest 新增/修改 优惠券
     * @return  com.taki.market.domain.dto.SaveOrUpdateCouponDTO
     * @author Long
     * @date: 2022/10/4 11:22
     */
    SaveOrUpdateCouponDTO saveOrUpdateCoupon(SaveOrUpdateCouponRequest saveOrUpdateCouponRequest);

    /*** 
     * @description:  领取优惠券
     * @param receiveCouponRequest 领取优惠券
     * @return  com.taki.market.domain.dto.ReceiveCouponDTO
     * @author Long
     * @date: 2022/10/4 21:53
     */ 
    ReceiveCouponDTO receiveCoupon(ReceiveCouponRequest receiveCouponRequest);

    /*** 
     * @description:  领取所有有效优惠券
     * @param userId
     * @return  java.lang.Object
     * @author Long
     * @date: 2022/10/4 23:23
     */ 
    Boolean receiveCouponAvailable(Long userId);

    /***
     * @description: 领取优惠券
     * @param sendCouponRequest 领取优惠券请求
     * @return
     * @author Long
     * @date: 2022/9/26 21:42
     */
    SendCouponDTO sendCoupon(SendCouponRequest sendCouponRequest);

    /***
     * @description: 根据选择条件发放换回优惠券
     * 此接口需要调用推送系统按条件推送消息的接口，
     * 传入选人条件，以及把优惠券+优惠券领取地址封装到一个对象中
     * @param sendCouponRequest 领取优惠券请求
     * @return
     * @author Long
     * @date: 2022/9/26 21:43
     */
    SendCouponDTO sendCouponByConditions(SendCouponRequest sendCouponRequest);
}
