package com.taki.market.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.market.domain.entity.MarketCouponItemDO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * <p>
 * 优惠券领取记录表 Mapper 接口
 * </p>
 *
 * @author long
 * @since 2022-09-24
 */
@Mapper
public interface
MarketCouponItemMapper extends BaseMapper<MarketCouponItemDO> {



    @Select("SELECT  coupon_id FROM  market_coupon_item " +
            "WHERE  user_id = #{userId}} AND  activity_end_time < #{now}" +
            "ORDER BY  coupon_id" +
            "LIMIT #{offset},#{limit}"
            )
    @ResultType(Long.class)
    List<Long> queryAvailableCoupon(@Param("userId") Long userId, @Param("now") LocalDateTime now,@Param("offset") Integer offset, @Param("limit") Integer limit);

}
