package com.taki.market.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.market.domain.entity.CouponDO;
import com.taki.market.mapper.CouponMapper;
import org.springframework.stereotype.Repository;

/**
 * @ClassName CouponDao
 * @Description 优惠券DAO组件
 * @Author Long
 * @Date 2022/2/18 11:26
 * @Version 1.0
 */
@Repository
public class CouponDao extends BaseDAO<CouponMapper, CouponDO> {


    /** 
     * @description: 查询用户优惠券
     * @param userId 用户Id
     * @param  couponId 优惠券Id
     * @return  com.taki.market.domain.entity.CouponDO
     * @author Long
     * @date: 2022/2/18 14:23
     */ 
    public CouponDO getUserCoupon(String userId,String couponId){

        return this.getOne(new QueryWrapper<CouponDO>().eq(CouponDO.USER_ID,userId).eq(CouponDO.COUPON_ID,couponId));
    }
}
