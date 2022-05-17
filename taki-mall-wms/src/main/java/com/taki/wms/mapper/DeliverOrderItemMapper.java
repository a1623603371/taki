package com.taki.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.wms.domain.entity.DeliveryOrderItemDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName DeliverOrderItemMapper
 * @Description 出库单 条目 Mapper 组件
 * @Author Long
 * @Date 2022/5/17 14:21
 * @Version 1.0
 */
@Mapper
public interface DeliverOrderItemMapper extends BaseMapper<DeliveryOrderItemDO> {
}
