package com.taki.order.dao;

import com.taki.common.BaseDAO;
import com.taki.order.domain.entity.AfterSaleItemDO;
import com.taki.order.domain.entity.OrderAmountDO;
import com.taki.order.mapper.AfterSaleItemMapper;
import com.taki.order.mapper.OrderAmountMapper;
import org.springframework.stereotype.Repository;

/**
 * @ClassName AfterSaleItemDao
 * @Description 售后条目 DAO 组件
 * @Author Long
 * @Date 2022/3/11 15:18
 * @Version 1.0
 */
@Repository
public class AfterSaleItemDao extends BaseDAO<AfterSaleItemMapper, AfterSaleItemDO> {

}
