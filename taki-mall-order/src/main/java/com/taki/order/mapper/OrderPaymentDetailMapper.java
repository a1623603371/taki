package com.taki.order.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.order.domin.entity.OrderPaymentDetailDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 订单⽀付明细表 Mapper 接口
 * </p>
 *
 * @author long
 * @since 2022-01-02
 */
@Mapper
public interface OrderPaymentDetailMapper extends BaseMapper<OrderPaymentDetailDO> {

}
