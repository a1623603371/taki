package com.taki.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.order.domain.entity.OrderAmountDO;
import com.taki.order.mapper.OrderAmountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName OrderAmountDao
 * @Description 订单 费用 DAO 组件
 * @Author Long
 * @Date 2022/1/2 16:16
 * @Version 1.0
 */
@Repository
@Slf4j
public class OrderAmountDao extends BaseDAO<OrderAmountMapper, OrderAmountDO> {


    public List<OrderAmountDO> listByOrderId(String orderId) {
        return this.list(new QueryWrapper<OrderAmountDO>().eq(OrderAmountDO.ORDER_ID,orderId));
    }
}
