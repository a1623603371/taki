package com.taki.market.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.market.domain.entity.MarketCouponItemDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 优惠券领取记录表 Mapper 接口
 * </p>
 *
 * @author long
 * @since 2022-09-24
 */
@Mapper
public interface MarketCouponItemMapper extends BaseMapper<MarketCouponItemDO> {

}
