package com.taki.market.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.market.domain.entity.CouponDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName CouponMapper
 * @Description 优惠券 mapper 组件
 * @Author Long
 * @Date 2022/2/18 11:16
 * @Version 1.0
 */
@Mapper
public interface CouponMapper extends BaseMapper<CouponDO> {
}
