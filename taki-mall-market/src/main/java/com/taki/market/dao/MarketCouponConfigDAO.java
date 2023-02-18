package com.taki.market.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.dao.BaseDAO;
import com.taki.market.domain.entity.MarketCouponConfigDO;
import com.taki.market.mapper.MarketCouponConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @ClassName MarketCouponConfigDAO
 * @Description 营销 优惠券配置
 * @Author Long
 * @Date 2022/9/24 22:43
 * @Version 1.0
 */
@Repository
public class MarketCouponConfigDAO extends BaseDAO<MarketCouponConfigMapper, MarketCouponConfigDO> {


    @Autowired
    private MarketCouponConfigMapper marketCouponConfigMapper;

    /***
     * @description:  获取优惠券 信息
     * @param couponConfigId 优惠券配置Id
     * @return
     * @author Long
     * @date: 2022/9/24 23:00
     */
    public  MarketCouponConfigDO getByCouponConfigId(Long couponConfigId) {

        return this.getOne(new QueryWrapper<MarketCouponConfigDO>().eq(MarketCouponConfigDO.ID,couponConfigId));
    }

    /***
     * @description:  保存 优惠券
     * @param marketCouponConfigDO 优惠券
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/10/4 11:55
     */
   public Boolean saveCoupon(MarketCouponConfigDO marketCouponConfigDO){
        return this.save(marketCouponConfigDO);
   }

   

}
