package com.taki.market.api.impl;

import com.taki.common.utli.ResponseData;
import com.taki.market.api.MarketApi;
import com.taki.market.domain.dto.*;
import com.taki.market.domain.request.*;
import com.taki.market.domain.query.UserCouponQuery;
import com.taki.market.exception.MarketBizException;
import com.taki.market.service.CouponService;
import com.taki.market.service.MarketService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName MarketApiImpl
 * @Description 营销系统API
 * @Author Long
 * @Date 2022/2/18 11:49
 * @Version 1.0
 */
@DubboService(version = "1.0.0",interfaceClass = MarketApi.class,retries = 0)
@Slf4j
public class MarketApiImpl implements MarketApi{


    @Autowired
    private CouponService couponService;

    @Autowired
    private MarketService marketService;


    @Override
    public ResponseData<CalculateOrderAmountDTO> calculateOrderAmount(CalculateOrderAmountRequest calculateOrderAmountRequest) {

        try {
            CalculateOrderAmountDTO calculateOrderAmountDTO =   marketService.calculateOrderAmount(calculateOrderAmountRequest);
            return ResponseData.success(calculateOrderAmountDTO);
        }catch (Exception e){
            log.error("biz error",e);
            return ResponseData.error(e.getMessage());
        }
    }

    @Override
    public ResponseData<Boolean> lockUserCoupon(LockUserCouponRequest lockUserCouponRequest) {
        try {
            Boolean result =   couponService.lockUserCoupon(lockUserCouponRequest);
            return ResponseData.success(result);
        }catch (Exception e){
            log.error("biz error",e);
            return ResponseData.error(e.getMessage());
        }
    }

    @Override
    public ResponseData<Boolean> releaseUserCoupon(ReleaseUserCouponRequest releaseUserCouponRequest) {
        try {
            Boolean result =   couponService.releaseUserCoupon(releaseUserCouponRequest);
            return ResponseData.success(result);
        }catch (Exception e){
            log.error("biz error",e);
            return ResponseData.error(e.getMessage());
        }
    }

    @Override
    public ResponseData<UserCouponDTO> queryUserCoupon(UserCouponQuery userCouponQuery) {
        try {
            UserCouponDTO userCouponDTO = couponService.getUserCoupon(userCouponQuery);
            return ResponseData.success(userCouponDTO);
        }catch (Exception e){
            log.error("biz error",e);
            return ResponseData.error(e.getMessage());
        }


    }

    @Override
    public ResponseData<Boolean> cancelOrderReleaseCoupon(CancelOrderReleaseUserCouponRequest cancelOrderReleaseUserCouponRequest) {
        log.info("回退优惠券,orderId:{}",cancelOrderReleaseUserCouponRequest.getOrderId());
         return ResponseData.success(true);
    }

    @Override
    public ResponseData<SaveOrUpdateCouponDTO> saveOrUpdateCoupon(SaveOrUpdateCouponRequest saveOrUpdateCouponRequest) {

        try {


            return ResponseData.success(couponService.saveOrUpdateCoupon(saveOrUpdateCouponRequest));
        }catch (MarketBizException e){

            log.error("biz error",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());

        }catch (Exception e){
            log.error("system error",e);

            return ResponseData.error(e.getMessage());
        }


    }

    @Override
    public ResponseData<ReceiveCouponDTO> receiveCoupon(ReceiveCouponRequest receiveCouponRequest) {
        try {


            return ResponseData.success(couponService.receiveCoupon(receiveCouponRequest));
        }catch (MarketBizException e){

            log.error("biz error",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());

        }catch (Exception e){
            log.error("system error",e);

            return ResponseData.error(e.getMessage());
        }

    }

    @Override
    public ResponseData<Boolean> receiveCouponAvailable(Long userId) {

        try {
            return ResponseData.success(couponService.receiveCouponAvailable(userId));
        }catch (MarketBizException e){

            log.error("biz error",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());

        }catch (Exception e){
            log.error("system error",e);

            return ResponseData.error(e.getMessage());
        }

    }

    @Override
    public ResponseData<SendCouponDTO> sendCoupon(SendCouponRequest sendCouponRequest) {
        try {
            return ResponseData.success(couponService.sendCoupon(sendCouponRequest));
        }catch (MarketBizException e){

            log.error("biz error",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());

        }catch (Exception e){
            log.error("system error",e);

            return ResponseData.error(e.getMessage());
        }
    }

    @Override
    public ResponseData<SendCouponDTO> sendCouponByConditions(SendCouponRequest sendCouponRequest) {
        try {
            return ResponseData.success(couponService.sendCouponByConditions(sendCouponRequest));
        }catch (MarketBizException e){

            log.error("biz error",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());

        }catch (Exception e){
            log.error("system error",e);

            return ResponseData.error(e.getMessage());
        }
    }
}
