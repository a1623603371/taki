package com.taki.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.order.domain.entity.AfterSaleInfoDO;
import com.taki.order.domain.entity.AfterSaleItemDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName AfterSaleItemMapper
 * @Description 订单售后条目 mapper 接口
 * @Author Long
 * @Date 2022/3/11 15:28
 * @Version 1.0
 */
@Mapper
public interface AfterSaleItemMapper  extends BaseMapper<AfterSaleItemDO> {

}
