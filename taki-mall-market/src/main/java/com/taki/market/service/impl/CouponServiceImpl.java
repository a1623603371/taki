package com.taki.market.service.impl;

import com.taki.common.utlis.ParamCheckUtil;
import com.taki.market.dao.CouponConfigDao;
import com.taki.market.dao.CouponDao;
import com.taki.market.domain.dto.UserCouponDTO;
import com.taki.market.domain.entity.CouponConfigDO;
import com.taki.market.domain.entity.CouponDO;
import com.taki.market.enums.CouponStatusEnum;
import com.taki.market.exception.MarketBizException;
import com.taki.market.exception.MarketErrorCodeEnum;
import com.taki.market.request.LockUserCouponRequest;
import com.taki.market.request.ReleaseUserCouponRequest;
import com.taki.market.domain.query.UserCouponQuery;
import com.taki.market.service.CouponService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;

/**
 * @ClassName CouponServiceImpl
 * @Description 优惠券 service组件
 * @Author Long
 * @Date 2022/2/18 14:18
 * @Version 1.0
 *
 */
@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponDao couponDao;


    @Autowired
    private CouponConfigDao couponConfigDao;



    @Override
    public UserCouponDTO getUserCoupon(UserCouponQuery userCouponQuery) {
    String userId = userCouponQuery.getUserId();
    ParamCheckUtil.checkStringNonEmpty(userId);
    String couponId = userCouponQuery.getCouponId();
    ParamCheckUtil.checkStringNonEmpty(couponId);

    CouponDO  coupon = couponDao.getUserCoupon(userId,couponId);

    if (ObjectUtils.isEmpty(coupon)){
        throw new MarketBizException(MarketErrorCodeEnum.USER_COUPON_IS_NULL);
    }
    String couponConfigId = coupon.getCouponConfigId();

    // 判断优惠券活动配置信息是否存在
    CouponConfigDO  couponConfig = couponConfigDao.getByCouponConfigId(couponConfigId);

    if (ObjectUtils.isEmpty(couponConfig)){
        throw new MarketBizException(MarketErrorCodeEnum.USER_COUPON_CONFIG_IS_NULL);
    }
    UserCouponDTO userCouponDTO = new UserCouponDTO();
    userCouponDTO.setUserId(userId);
    userCouponDTO.setCouponId(couponId);
    userCouponDTO.setName(couponConfig.getName());
    userCouponDTO.setAmount(couponConfig.getAmount());
    userCouponDTO.setType(couponConfig.getType());
    userCouponDTO.setCouponConfigId(couponConfigId);
    userCouponDTO.setConditionAmount(couponConfig.getConditionAmount());
    userCouponDTO.setValidStartTime(couponConfig.getValidStartTime());
    userCouponDTO.setValidEndTime(couponConfig.getValidEndTime());
        return  userCouponDTO;
    }

    @Override
    public Boolean lockUserCoupon(LockUserCouponRequest lockUserCouponRequest) {
        // 检查入参
        checkLockUserCouponRequest(lockUserCouponRequest);

        String userId = lockUserCouponRequest.getUserId();
        String couponId = lockUserCouponRequest.getCouponId();

        CouponDO couponDO = couponDao.getUserCoupon(userId,couponId);
        if (ObjectUtils.isEmpty(couponDO)){
            throw new MarketBizException(MarketErrorCodeEnum.USER_COUPON_IS_NULL);
        }

        // 判断优惠券是否已使用
        if (CouponStatusEnum.USED.getCode().equals(couponDO.getUsed())){
            throw new MarketBizException(MarketErrorCodeEnum.USER_COUPON_IS_USED);
        }
        couponDO.setUsed(CouponStatusEnum.USED.getCode());
        couponDO.setUsedTime(LocalDateTime.now());
        couponDao.updateById(couponDO);
        return true;
    }

    /**
     * @description:  检查锁定用户优惠券 入参
     * @param lockUserCouponRequest 锁定用户优惠券 请求
     * @return  void
     * @author Long
     * @date: 2022/2/18 15:14
     */
    private void checkLockUserCouponRequest(LockUserCouponRequest lockUserCouponRequest) {
        String userId = lockUserCouponRequest.getUserId();
        String couponId = lockUserCouponRequest.getCouponId();
        ParamCheckUtil.checkStringNonEmpty(userId);
        ParamCheckUtil.checkStringNonEmpty(couponId);
    }

    @Override
    public Boolean releaseUserCoupon(ReleaseUserCouponRequest releaseUserCouponRequest) {

        String userId = releaseUserCouponRequest.getUserId();
        String couponId = releaseUserCouponRequest.getCouponId();
        CouponDO couponDO = couponDao.getUserCoupon(userId,couponId);
        couponDO.setUsed(CouponStatusEnum.UN_USED.getCode());
        couponDO.setUsedTime(null);
        couponDao.updateById(couponDO);
        return true;
    }
}
