package com.taki.wms.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.dao.BaseDAO;
import com.taki.wms.domain.entity.DeliveryOrderItemDO;
import com.taki.wms.mapper.DeliverOrderItemMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName DeliveryOrderItemDao
 * @Description TODO
 * @Author Long
 * @Date 2022/5/16 20:34
 * @Version 1.0
 */
@Repository
public class DeliveryOrderItemDao extends BaseDAO<DeliverOrderItemMapper, DeliveryOrderItemDO> {

    /**
     * @description: 根据 出货单Id 查询 出货单条目
     * @param deliveryOrderIds 出货单Id 集合
     * @return
     * @author Long
     * @date: 2022/5/17 14:30
     */
    public List<DeliveryOrderItemDO> listByDeliveryOrderIds(List<String> deliveryOrderIds) {

        return this.list(new QueryWrapper<DeliveryOrderItemDO>().in(DeliveryOrderItemDO.DELIVERY_ORDER_ID,deliveryOrderIds));
    }
}
