package com.taki.wms.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.wms.domain.entity.DeliverOrderDO;
import com.taki.wms.mapper.DeliverOrderMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName DeliverOrderDao
 * @Description 出库单 dao 组件
 * @Author Long
 * @Date 2022/5/16 20:23
 * @Version 1.0
 */
@Repository
public class DeliverOrderDao extends BaseDAO<DeliverOrderMapper, DeliverOrderDO> {

    /**
     * @description: 根据订单Id 查询 出库单条目
     * @param orderId 订单Id
     * @return
     * @author Long
     * @date: 2022/5/17 14:26
     */
    public List<DeliverOrderDO> listByOrderId(String orderId) {

        return this.list(new QueryWrapper<DeliverOrderDO>().eq(DeliverOrderDO.ORDER_ID,orderId));
    }
}
