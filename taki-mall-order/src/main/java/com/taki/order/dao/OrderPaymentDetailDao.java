package com.taki.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.order.domin.entity.OrderInfoDO;
import com.taki.order.domin.entity.OrderPaymentDetailDO;
import com.taki.order.mapper.OrderPaymentDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @ClassName OrderPaymentDetailDao
 * @Description 订单支付明细 DAO 组件
 * @Author Long
 * @Date 2022/1/2 16:20
 * @Version 1.0
 */
@Slf4j
@Repository
public class OrderPaymentDetailDao extends BaseDAO<OrderPaymentDetailMapper, OrderPaymentDetailDO> {

    /**
     * @description: 根据订单Id 查询 支付详细信息
     * @param orderId 订单Id
     * @return  订单支付详细信息
     * @author Long
     * @date: 2022/1/16 13:42
     */
    public OrderPaymentDetailDO getPaymentDetailByOrderId(String orderId) {

      return  this.getOne(new QueryWrapper<OrderPaymentDetailDO>().eq(OrderPaymentDetailDO.ORDER_ID,orderId));
    }
}
