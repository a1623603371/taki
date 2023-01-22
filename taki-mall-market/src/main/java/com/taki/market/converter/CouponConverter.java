package com.taki.market.converter;

import com.taki.market.domain.dto.MarketCouponItemDTO;
import com.taki.market.domain.entity.MarketCouponConfigDO;
import com.taki.market.domain.entity.MarketCouponItemDO;
import com.taki.market.domain.request.SaveOrUpdateCouponRequest;
import com.taki.market.domain.request.SendCouponRequest;
import org.mapstruct.Mapper;

/**
 * @ClassName CouponConverter
 * @Description 优惠券转换类
 * @Author Long
 * @Date 2022/10/4 11:27
 * @Version 1.0
 */
@Mapper( componentModel = "spring")
public interface CouponConverter {


    MarketCouponConfigDO converterCouponDO(SaveOrUpdateCouponRequest saveOrUpdateCouponRequest);


    MarketCouponConfigDO converterCouponDO(SendCouponRequest sendCouponRequest);


    MarketCouponItemDO converterCouponItemDO(MarketCouponItemDTO marketCouponItemDTO);
}
