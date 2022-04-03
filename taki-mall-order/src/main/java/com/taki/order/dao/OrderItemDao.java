package com.taki.order.dao;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.order.domain.dto.OrderItemDTO;
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



    /**
     * @description: 根据商品id 查询订单条目集合
     * @param orderId 订单iD
     * @return  订单条目集合
     * @author Long
     * @date: 2022/4/3 21:02
     */
    public List<OrderItemDO> listByOrderId(String orderId) {

        return this.list(new QueryWrapper<OrderItemDO>().eq(OrderItemDO.ORDER_ID,orderId));
    }

    /**
     * @description: 根据 订单Id 与skuCode 查询 商品条目
     * @param orderId 订单Id
     * @param  skuCode 商品编码
     * @return  商品条目
     * @author Long
     * @date: 2022/4/3 21:03
     */
    public OrderItemDO getOrderItemBySkuAndOrderId(String orderId, String skuCode) {
        return this.getOne(new QueryWrapper<OrderItemDO>().eq(OrderItemDO.ORDER_ID,orderId).eq(OrderItemDO.SKU_CODE,skuCode));

    }
}
