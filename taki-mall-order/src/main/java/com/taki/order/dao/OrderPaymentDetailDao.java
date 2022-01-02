package com.taki.order.dao;

import com.taki.common.BaseDAO;
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
}
