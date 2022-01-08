package com.taki.market.api;

import com.taki.common.utlis.ResponseData;
import com.taki.market.domain.dto.CalculateOrderAmountDTO;
import com.taki.market.domain.dto.UserCouponDTO;
import com.taki.market.request.CalculateOrderAmountRequest;
import com.taki.market.request.LockUserCouponRequest;
import com.taki.market.request.UserCouponQuery;

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
     *  查询用户优惠券
     * @param userCouponQuery 查询条件
     * @return
     */
    ResponseData<UserCouponDTO> queryUserCoupon(UserCouponQuery userCouponQuery);
}
