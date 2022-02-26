package com.taki.market.api;

import com.taki.common.utlis.ResponseData;
import com.taki.market.domain.dto.CalculateOrderAmountDTO;
import com.taki.market.domain.dto.UserCouponDTO;
import com.taki.market.request.CalculateOrderAmountRequest;
import com.taki.market.request.CancelOrderReleaseUserCouponRequest;
import com.taki.market.request.LockUserCouponRequest;
import com.taki.market.domain.query.UserCouponQuery;
import com.taki.market.request.ReleaseUserCouponRequest;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @ClassName MarketApiImpl
 * @Description TODO
 * @Author Long
 * @Date 2022/2/18 11:49
 * @Version 1.0
 */
@DubboService(version = "1.0.0",interfaceClass = MarketApi.class,retries = 0)
public class MarketApiImpl implements MarketApi{
    @Override
    public ResponseData<CalculateOrderAmountDTO> calculateOrderAmount(CalculateOrderAmountRequest calculateOrderAmountRequest) {
        return null;
    }

    @Override
    public ResponseData<Boolean> lockUserCoupon(LockUserCouponRequest lockUserCouponRequest) {
        return null;
    }

    @Override
    public ResponseData<Boolean> releaseUserCoupon(ReleaseUserCouponRequest releaseUserCouponRequest) {
        return null;
    }

    @Override
    public ResponseData<UserCouponDTO> queryUserCoupon(UserCouponQuery userCouponQuery) {
        return null;
    }

    @Override
    public ResponseData<Boolean> cancelOrderReleaseCoupon(CancelOrderReleaseUserCouponRequest cancelOrderReleaseUserCouponRequest) {
        return null;
    }
}
