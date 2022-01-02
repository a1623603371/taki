package com.taki.order.dao;

import com.taki.common.BaseDAO;
import com.taki.order.domin.entity.OrderOperateLogDO;
import com.taki.order.mapper.OrderOperateLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 * @ClassName OrderOperateLogDao
 * @Description 订单操作 DAO 组件
 * @Author Long
 * @Date 2022/1/2 16:19
 * @Version 1.0
 */
@Repository
@Slf4j
public class OrderOperateLogDao extends BaseDAO<OrderOperateLogMapper, OrderOperateLogDO> {
}
