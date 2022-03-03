package com.taki.order.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.order.domain.entity.OrderAmountDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单费⽤表 Mapper 接口
 * </p>
 *
 * @author long
 * @since 2022-01-02
 */
@Mapper
public interface OrderAmountMapper extends BaseMapper<OrderAmountDO> {

}
