package com.taki.order.dao;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.order.domain.entity.OrderItemDO;
import com.taki.order.mapper.OrderItemMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName OrderItemDao
 * @Description 订单 详情 DAO 组件
 * @Author Long
 * @Date 2022/1/2 16:13
 * @Version 1.0
 */
@Slf4j
@Repository
public class OrderItemDao  extends BaseDAO<OrderItemMapper, OrderItemDO> {




    public List<OrderItemDO> listByOrderId(String orderId) {

        return this.list(new QueryWrapper<OrderItemDO>().eq(OrderItemDO.ORDER_ID,orderId));
    }
}
