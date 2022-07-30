package com.taki.market.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.market.domain.entity.CouponConfigDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName CouponConfigMapper
 * @Description 优惠券配置
 * @Author Long
 * @Date 2022/2/18 10:31
 * @Version 1.0
 */
@Mapper
public interface CouponConfigMapper extends BaseMapper<CouponConfigDO> {
}
