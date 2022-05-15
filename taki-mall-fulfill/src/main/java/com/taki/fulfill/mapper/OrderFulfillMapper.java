package com.taki.fulfill.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.fulfill.domain.entity.OrderFulfillDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName OrderFulfillMapper
 * @Description 订单 履约表 mapper 组件
 * @Author Long
 * @Date 2022/5/15 17:01
 * @Version 1.0
 */

@Mapper
public interface OrderFulfillMapper extends BaseMapper<OrderFulfillDO> {


}
