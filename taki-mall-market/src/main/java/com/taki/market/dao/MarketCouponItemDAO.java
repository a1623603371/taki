package com.taki.market.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.market.domain.entity.MarketCouponConfigDO;
import com.taki.market.domain.entity.MarketCouponItemDO;
import com.taki.market.mapper.MarketCouponConfigMapper;
import com.taki.market.mapper.MarketCouponItemMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @ClassName MarketCouponItemDAO
 * @Description 营销 优惠券领取记录
 * @Author Long
 * @Date 2022/9/24 22:48
 * @Version 1.0
 */
@Repository
public class MarketCouponItemDAO extends BaseDAO<MarketCouponItemMapper, MarketCouponItemDO> {


    @Autowired
    private MarketCouponItemMapper marketCouponItemMapper;


    /***
     * @description: 获取用户优惠券
     * @param userId 用户id
     * @param couponId 优惠券id
     * @return
     * @author Long
     * @date: 2022/9/24 22:51
     */
    public MarketCouponItemDO getUserCoupon(String userId,String couponId){

        return  this.getOne(new QueryWrapper<MarketCouponItemDO>().eq(MarketCouponItemDO.USER_ID,userId).eq(MarketCouponItemDO.COUPON_ID,couponId));
    }

}
