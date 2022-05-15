package com.taki.fulfill.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.fulfill.domain.entity.OrderFulfillDO;
import com.taki.fulfill.domain.entity.OrderFulfillItemDO;
import com.taki.fulfill.mapper.OrderFulfillItemMapper;
import com.taki.fulfill.mapper.OrderFulfillMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName OrderFulfillItemDao
 * @Description 订单 履约条目
 * @Author Long
 * @Date 2022/5/15 18:55
 * @Version 1.0
 */
@Repository
public class OrderFulfillItemDao extends BaseDAO<OrderFulfillItemMapper, OrderFulfillItemDO> {

    /**
     * @description: 根据 履约订单Id 查询 履约条目集合
     * @param fulFillId 履约订单Id
     * @return
     * @author Long
     * @date: 2022/5/15 21:23
     */

    public List<OrderFulfillItemDO> listByFulfillId(String fulFillId) {

        return this.list(new QueryWrapper<OrderFulfillItemDO>().eq(OrderFulfillItemDO.FULFILL_ID,fulFillId));
    }
}
