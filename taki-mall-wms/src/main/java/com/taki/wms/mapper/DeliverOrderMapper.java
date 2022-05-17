package com.taki.wms.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.wms.domain.entity.DeliverOrderDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName DeliverOrderMapper
 * @Description  出库单 mapper组件
 * @Author Long
 * @Date 2022/5/16 20:24
 * @Version 1.0
 */
@Mapper
public interface DeliverOrderMapper extends BaseMapper<DeliverOrderDO> {
}
