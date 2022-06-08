package com.taki.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.order.domain.entity.OrderPaymentDetailDO;
import com.taki.order.mapper.OrderPaymentDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

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

    /**
     * @description:  根据订单Id 查询 订单支付信息
     * @param orderId 订单Id'
     * @return  订单支付信息
     * @author Long
     * @date: 2022/3/3 21:22
     */
    public List<OrderPaymentDetailDO> listByOrderId(String orderId) {

        return this.list(new QueryWrapper<OrderPaymentDetailDO>().eq(OrderPaymentDetailDO.ORDER_ID,orderId));
    }

    /**
     * @description:   更新订单修改 订单支付明细
     * @param orderPaymentDetailDO 订单支付明细信息
     * @param orderId 订单Id
     * @return  void
     * @author Long
     * @date: 2022/6/8 14:44
     */
    public Boolean updateByOrderId(OrderPaymentDetailDO orderPaymentDetailDO, String orderId) {

        return this.update(orderPaymentDetailDO,new QueryWrapper<OrderPaymentDetailDO>().eq(OrderPaymentDetailDO.ORDER_ID,orderId));

    }

    /**
     * @description:   批量更新订单修改 订单支付明细
     * @param orderPaymentDetailDO 订单支付明细信息
     * @param orderIds 订单Id集合
     * @return  void
     * @author Long
     * @date: 2022/6/8 14:44
     */
    public Boolean updateBatchByOrderId(OrderPaymentDetailDO orderPaymentDetailDO, List<String> orderIds) {

        return this.update(orderPaymentDetailDO,new QueryWrapper<OrderPaymentDetailDO>().in(OrderPaymentDetailDO.ORDER_ID,orderIds));
    }
}
