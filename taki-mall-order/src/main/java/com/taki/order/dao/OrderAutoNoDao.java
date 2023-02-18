package com.taki.order.dao;

import com.taki.common.dao.BaseDAO;

import com.taki.order.domain.entity.OrderAutoNoDO;
import com.taki.order.mapper.OrderAutoNoMapper;
import org.springframework.stereotype.Repository;

/**
 * @ClassName OrderAutoNoDao
 * @Description 订单id 生成 DAO 组件
 * @Author Long
 * @Date 2022/1/2 21:55
 * @Version 1.0
 */
@Repository
public class OrderAutoNoDao extends BaseDAO<OrderAutoNoMapper, OrderAutoNoDO> {
}
