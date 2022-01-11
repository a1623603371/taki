package com.taki.order.dao;

import com.taki.common.BaseDAO;
import com.taki.order.domin.entity.OrderAutoNoDO;
import com.taki.order.domin.entity.OrderDeliveryDetailDO;
import com.taki.order.mapper.OrderAutoNoMapper;
import com.taki.order.mapper.OrderDeliveryDetailMapper;
import org.springframework.stereotype.Repository;

/**
 * @ClassName OrderDeliberyDetailDao
 * @Description TODO
 * @Author Long
 * @Date 2022/1/11 21:51
 * @Version 1.0
 */
@Repository
public class OrderDeliveryDetailDao extends BaseDAO<OrderDeliveryDetailMapper, OrderDeliveryDetailDO> {
}
