package com.taki.order.remote;

import com.taki.common.utli.ResponseData;
import com.taki.market.api.MarketApi;
import com.taki.market.domain.dto.CalculateOrderAmountDTO;
import com.taki.market.domain.dto.UserCouponDTO;
import com.taki.market.domain.query.UserCouponQuery;
import com.taki.market.request.CalculateOrderAmountRequest;
import com.taki.market.request.CancelOrderReleaseUserCouponRequest;
import com.taki.market.request.LockUserCouponRequest;
import com.taki.market.request.ReleaseUserCouponRequest;
import com.taki.order.exception.OrderBizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @ClassName marketRemote
 * @Description 营销系统远程api
 * @Author Long
 * @Date 2022/6/8 15:13
 * @Version 1.0
 */
@Component
@Slf4j
public class MarketRemote {

    @DubboReference(version = "1.0.0",retries = 0)
    private MarketApi marketApi;


    /**
     * @description: 调用 计算 订单 费用接口
     * @param calculateOrderAmountRequest 计算 订单 费用 请求 参数
     * @return
     * @author Long
     * @date: 2022/6/8 15:15
     */
    public CalculateOrderAmountDTO calculateOrderAmount(CalculateOrderAmountRequest calculateOrderAmountRequest){

        ResponseData<CalculateOrderAmountDTO> responseResult = marketApi.calculateOrderAmount(calculateOrderAmountRequest);

        if (!responseResult.getSuccess()){
            log.error("调用营销服务计算价格失败错误信息：{}",responseResult.getMessage());
            throw new OrderBizException(responseResult.getCode(),responseResult.getMessage());
        }
        return responseResult.getData();
    }

    /**
     * @description: 锁定用户优惠券
     * @param lockUserCouponRequest 锁定用户优惠券请求
     * @return  锁定请求响应
     * @author Long
     * @date: 2022/1/6 9:52
     */
    public Boolean lockUserCoupon(LockUserCouponRequest lockUserCouponRequest){
        ResponseData<Boolean> result = marketApi.lockUserCoupon(lockUserCouponRequest);

        if (!result.getSuccess()){
            log.error("锁定优惠券失败,订单号:{},优惠券Id:{},用户Id:{}",lockUserCouponRequest.getOrderId(),lockUserCouponRequest.getCouponId(),lockUserCouponRequest.getUserId());
            throw new OrderBizException(result.getCode(),result.getMessage());
        }

        return result.getData();
    }
    /**
     * @description: 释放用户优惠券
     * @param releaseUserCouponRequest 释放用户优惠券请求
     * @return  处理结果
     * @author Long
     * @date: 2022/2/18 14:03
     */
    public Boolean releaseUserCoupon(ReleaseUserCouponRequest releaseUserCouponRequest){
        ResponseData<Boolean> result = marketApi.releaseUserCoupon(releaseUserCouponRequest);
        if (!result.getSuccess()){
            throw new OrderBizException(result.getCode(),result.getMessage());
        }
        return result.getData();
    }


    /**
     *  查询用户优惠券
     * @param userCouponQuery 查询条件
     * @return
     */
    public UserCouponDTO queryUserCoupon(UserCouponQuery userCouponQuery){
        ResponseData<UserCouponDTO> result = marketApi.queryUserCoupon(userCouponQuery);
        if (!result.getSuccess()){
            throw new OrderBizException(result.getCode(),result.getMessage());
        }
        return result.getData();
    }


    /**
     * @description: 取消订单 释放已使用用户优惠券
     * @param cancelOrderReleaseUserCouponRequest 取消订单 释放已使用用户优惠券 请求
     * @return  com.taki.common.utlis.ResponseData<java.lang.Boolean>
     * @author Long
     * @date: 2022/2/18 14:08
     */
     public  Boolean cancelOrderReleaseCoupon(CancelOrderReleaseUserCouponRequest cancelOrderReleaseUserCouponRequest){
         ResponseData<Boolean> result = marketApi.cancelOrderReleaseCoupon(cancelOrderReleaseUserCouponRequest);
         if (!result.getSuccess()){
             throw new OrderBizException(result.getCode(),result.getMessage());
         }
         return result.getData();
     }

}
