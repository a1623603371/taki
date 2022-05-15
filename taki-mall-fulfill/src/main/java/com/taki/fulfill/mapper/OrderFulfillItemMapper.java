package com.taki.fulfill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.fulfill.domain.entity.OrderFulfillItemDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName OrderFulfillItemMapper
 * @Description 订单 履约 条目
 * @Author Long
 * @Date 2022/5/15 18:52
 * @Version 1.0
 */
@Mapper
public interface OrderFulfillItemMapper extends BaseMapper<OrderFulfillItemDO> {
}
