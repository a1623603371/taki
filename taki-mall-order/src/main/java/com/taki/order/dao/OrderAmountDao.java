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

    /**
     * @description: 根据订单Id 查询 订单费用信息
     * @param orderId 订单ID
     * @return  java.util.List<com.taki.order.domain.entity.OrderAmountDO>
     * @author Long
     * @date: 2022/4/3 20:55
     */
    public List<OrderAmountDO> listByOrderId(String orderId) {
        return this.list(new QueryWrapper<OrderAmountDO>().eq(OrderAmountDO.ORDER_ID,orderId));
    }


    /**
     * @description:  根据订单Id 与支付类型 查询 订单费用信息
     * @param orderId 订单Id
     * @param amountType 费用类型
     * @return 订单费用信息
     * @author Long
     * @date: 2022/4/3 20:55
     */
    public OrderAmountDO getByIdAndAmountType(String orderId,Integer amountType) {

        return this.getOne(new QueryWrapper<OrderAmountDO>().eq(OrderAmountDO.ORDER_ID,orderId).eq(OrderAmountDO.AMOUNT_TYPE,amountType));
    }


}
