package com.taki.order.dao;


import com.taki.common.BaseDAO;
import com.taki.order.domin.entity.OrderItemDO;
import com.taki.order.mapper.OrderItemMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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
}
