package com.taki.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.order.domain.entity.OrderOperateLogDO;
import com.taki.order.mapper.OrderOperateLogMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

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


    public List<OrderOperateLogDO> listByOrderId(String orderId) {

        return this.list(new QueryWrapper<OrderOperateLogDO>().eq(OrderOperateLogDO.ORDER_ID,orderId));
    }
}
