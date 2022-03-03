package com.taki.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.order.domain.entity.OrderAmountDetailDO;
import com.taki.order.mapper.OrderAmountDetailMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName OrderAmountDetailDao
 * @Description 订单 费用 详情 DAO
 * @Author Long
 * @Date 2022/1/2 16:18
 * @Version 1.0
 */
@Slf4j
@Repository
public class OrderAmountDetailDao extends BaseDAO<OrderAmountDetailMapper, OrderAmountDetailDO> {

    @Autowired
    private  OrderAmountDetailMapper orderAmountDetailMapper;

    public List<OrderAmountDetailDO> listByOrderId(String orderId) {
        return this.list(new QueryWrapper<OrderAmountDetailDO>().eq(OrderAmountDetailDO.ORDER_ID,orderId));
    }
}
