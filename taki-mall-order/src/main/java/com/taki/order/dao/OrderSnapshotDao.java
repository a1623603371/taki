package com.taki.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.dao.BaseDAO;
import com.taki.order.domain.entity.OrderSnapshotDO;
import com.taki.order.mapper.OrderSnapshotMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName OrderSnapshotDao
 * @Description 订单 快照  组件
 * @Author Long
 * @Date 2022/1/2 16:22
 * @Version 1.0
 */
@Repository
public class OrderSnapshotDao extends BaseDAO<OrderSnapshotMapper, OrderSnapshotDO> {

    public List<OrderSnapshotDO> listByOrderId(String orderId) {

        return this.list(new QueryWrapper<OrderSnapshotDO>().eq(OrderSnapshotDO.ORDER_ID,orderId));
    }
}
