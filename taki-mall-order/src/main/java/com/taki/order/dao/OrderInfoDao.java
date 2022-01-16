package com.taki.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.order.domin.entity.OrderInfoDO;
import com.taki.order.mapper.OrderInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName OrderInfoDao
 * @Description 订单 DAO 组件
 * @Author Long
 * @Date 2022/1/2 16:10
 * @Version 1.0
 */
@Repository
@Slf4j
public class OrderInfoDao extends BaseDAO<OrderInfoMapper, OrderInfoDO> {




    /**
     * @description: 根据订单Id查询订单
     * @param orderId 订单Id
     * @return  订单信息
     * @author Long
     * @date: 2022/1/16 15:37
     */
    public OrderInfoDO getByOrderId(String orderId) {

      return  this.getOne(new QueryWrapper<OrderInfoDO>().eq(OrderInfoDO.ORDER_ID,orderId));
    }

    /**
     * @description: 根据订单Id查询子订单
     * @param orderId 订单Id
     * @return  子订单集合
     * @author Long
     * @date: 2022/1/16 15:38
     */
    public List<OrderInfoDO> listByParentOrderId(String orderId) {

        return this.list(new QueryWrapper<OrderInfoDO>().eq(OrderInfoDO.PARENT_ORDER_ID,orderId));

    }
}
