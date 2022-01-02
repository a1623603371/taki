package com.taki.order.dao;

import com.taki.common.BaseDAO;
import com.taki.order.domin.entity.OrderInfoDO;
import com.taki.order.mapper.OrderInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

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
}
