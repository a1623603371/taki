package com.taki.market.api;

import com.taki.common.utli.ResponseData;
import com.taki.market.domain.dto.*;
import com.taki.market.domain.request.*;
import com.taki.market.domain.query.UserCouponQuery;

/**
 * @ClassName MarketApi
 * @Description 营销系统 API
 * @Author Long
 * @Date 2022/1/5 10:01
 * @Version 1.0
 */
public interface MarketApi {


    /** 
     * @description: 计算订单费用
     * @param calculateOrderAmountRequest 计算订单费用请求
     * @return 订单费用结果
     * @author Long
     * @date: 2022/1/5 10:19
     */ 
    ResponseData<CalculateOrderAmountDTO> calculateOrderAmount(CalculateOrderAmountRequest calculateOrderAmountRequest);

    /**
     * @description: 锁定用户优惠券
     * @param lockUserCouponRequest 锁定用户优惠券请求
     * @return  锁定请求响应
     * @author Long
     * @date: 2022/1/6 9:52
     */
    ResponseData<Boolean> lockUserCoupon(LockUserCouponRequest lockUserCouponRequest);


    /**
     * @description: 释放用户优惠券
     * @param releaseUserCouponRequest 释放用户优惠券请求
     * @return  处理结果
     * @author Long
     * @date: 2022/2/18 14:03
     */
    ResponseData<Boolean> releaseUserCoupon(ReleaseUserCouponRequest releaseUserCouponRequest);

    /**
     *  查询用户优惠券
     * @param userCouponQuery 查询条件
     * @return
     */
    ResponseData<UserCouponDTO> queryUserCoupon(UserCouponQuery userCouponQuery);


    /** 
     * @description: 取消订单 释放已使用用户优惠券
     * @param cancelOrderReleaseUserCouponRequest 取消订单 释放已使用用户优惠券 请求
     * @return  com.taki.common.utlis.ResponseData<java.lang.Boolean>
     * @author Long
     * @date: 2022/2/18 14:08
     */ 
    ResponseData<Boolean>cancelOrderReleaseCoupon(CancelOrderReleaseUserCouponRequest cancelOrderReleaseUserCouponRequest);




    /*** 
     * @description:  新增/修改优惠券
     * @param saveOrUpdateCouponRequest
     * @return
     * @date: 2022/10/3 23:57
     */ 
    ResponseData<SaveOrUpdateCouponDTO> saveOrUpdateCoupon(SaveOrUpdateCouponRequest saveOrUpdateCouponRequest);

    /***
     * @description:  领取优惠券
     * @param receiveCouponRequest 领取优惠券 请求参数
     * @return
     * @author Long
     * @date: 2022/9/26 21:24
     */
    ResponseData<ReceiveCouponDTO> receiveCoupon(ReceiveCouponRequest receiveCouponRequest);

    /***
     * @description: 领 取当前正在活动中的优惠券
     * @param userId 用户Id
     * @return
     * @author Long
     * @date: 2022/9/26 21:29
     */
    ResponseData<Boolean> receiveCouponAvailable(Long userId);

    /***
     * @description: 领取优惠券
     * @param sendCouponRequest 领取优惠券请求
     * @return
     * @author Long
     * @date: 2022/9/26 21:42
     */
    ResponseData<SendCouponDTO> sendCoupon(SendCouponRequest sendCouponRequest);

    /***
     * @description: 根据选择条件发放换回优惠券
     * 此接口需要调用推送系统按条件推送消息的接口，
     * 传入选人条件，以及把优惠券+优惠券领取地址封装到一个对象中
     * @param sendCouponRequest 领取优惠券请求
     * @return
     * @author Long
     * @date: 2022/9/26 21:43
     */
    ResponseData<SendCouponDTO> sendCouponByConditions(SendCouponRequest sendCouponRequest);



}
