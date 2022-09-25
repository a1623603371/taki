package com.taki.market.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.taki.common.utli.ParamCheckUtil;
import com.taki.market.dao.MarketCouponConfigDAO;
import com.taki.market.dao.MarketCouponItemDAO;
import com.taki.market.domain.dto.UserCouponDTO;
import com.taki.market.domain.entity.MarketCouponConfigDO;
import com.taki.market.domain.entity.MarketCouponItemDO;
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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

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
    private MarketCouponItemDAO marketCouponItemDAO;


    @Autowired
    private MarketCouponConfigDAO marketCouponConfigDAO;



    @Override
    public UserCouponDTO getUserCoupon(UserCouponQuery userCouponQuery) {
    String userId = userCouponQuery.getUserId();
    ParamCheckUtil.checkStringNonEmpty(userId);
    String couponId = userCouponQuery.getCouponId();
    ParamCheckUtil.checkStringNonEmpty(couponId);

    MarketCouponItemDO marketCouponItem = marketCouponItemDAO.getUserCoupon(userId,couponId);

    if (ObjectUtils.isEmpty(marketCouponItem)){
        throw new MarketBizException(MarketErrorCodeEnum.USER_COUPON_IS_NULL);
    }
    String couponConfigId = marketCouponItem.getCouponId();

    // 判断优惠券活动配置信息是否存在
    MarketCouponConfigDO couponConfig = marketCouponConfigDAO.getByCouponConfigId(couponConfigId);

    if (ObjectUtils.isEmpty(couponConfig)){
        throw new MarketBizException(MarketErrorCodeEnum.USER_COUPON_CONFIG_IS_NULL);
    }

    String  rule = couponConfig.getCouponRule();
    Map<String,String> ruleMap = JSON.parseObject(rule,Map.class);

    UserCouponDTO userCouponDTO = new UserCouponDTO();
    userCouponDTO.setUserId(userId);
    userCouponDTO.setCouponId(couponId);
    userCouponDTO.setCouponName(couponConfig.getCouponName());
    userCouponDTO.setCouponType(couponConfig.getCouponType());
    userCouponDTO.setAmount(new BigDecimal(ruleMap.get("amount")));
    userCouponDTO.setConditionAmount(new BigDecimal(ruleMap.get("conditionAmount")));
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

        MarketCouponItemDO marketCouponItem = marketCouponItemDAO.getUserCoupon(userId,couponId);
        if (ObjectUtils.isEmpty(marketCouponItem)){
            throw new MarketBizException(MarketErrorCodeEnum.USER_COUPON_IS_NULL);
        }

        // 判断优惠券是否已使用
        if (CouponStatusEnum.USED.getCode().equals(marketCouponItem.getUsed())){
            throw new MarketBizException(MarketErrorCodeEnum.USER_COUPON_IS_USED);
        }
        marketCouponItem.setUsed(CouponStatusEnum.USED.getCode());
        marketCouponItem.setUsedTime(LocalDateTime.now());
        marketCouponItemDAO.updateById(marketCouponItem);
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
        MarketCouponItemDO marketCouponItem = marketCouponItemDAO.getUserCoupon(userId,couponId);
        marketCouponItem.setUsed(CouponStatusEnum.UN_USED.getCode());
        marketCouponItem.setUsedTime(null);
       return marketCouponItemDAO.updateById(marketCouponItem);
    }
}
