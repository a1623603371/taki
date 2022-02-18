package com.taki.market.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.market.domain.entity.CouponConfigDO;
import com.taki.market.mapper.CouponConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @ClassName CouponConfigDao
 * @Description 优惠券 配置 DAO 组件
 * @Author Long
 * @Date 2022/2/18 11:23
 * @Version 1.0
 */
@Repository
public class CouponConfigDao extends BaseDAO<CouponConfigMapper, CouponConfigDO> {


    @Autowired
    private CouponConfigMapper couponConfigMapper;


    public  CouponConfigDO getByCouponConfigId(String couponConfigId) {

        return  this.getOne(new QueryWrapper<CouponConfigDO>().eq(CouponConfigDO.COUPON_CONFIG_ID,couponConfigId));


    }
}
