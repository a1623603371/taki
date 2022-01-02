package com.taki.order.dao;

import com.taki.common.BaseDAO;
import com.taki.order.domin.entity.OrderAmountDetailDO;
import com.taki.order.mapper.OrderAmountDetailMapper;
import com.taki.order.mapper.OrderAmountMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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
}
