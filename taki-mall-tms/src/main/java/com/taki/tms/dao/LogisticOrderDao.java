package com.taki.tms.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.tms.domain.entity.LogisticOrderDO;
import com.taki.tms.mapper.LogisticOrderMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName LogisticOrderDao
 * @Description 物流订单 DAO 组件
 * @Author Long
 * @Date 2022/5/17 15:06
 * @Version 1.0
 */
@Repository
public class LogisticOrderDao extends BaseDAO<LogisticOrderMapper, LogisticOrderDO> {

    /**
     * @description: 根据订单Id 查询 物流单
     * @param orderId
     * @return
     * @author Long
     * @date: 2022/5/17 15:36
     */
    public List<LogisticOrderDO> listByOrderId(String orderId) {

        return this.list(new QueryWrapper<LogisticOrderDO>().eq(LogisticOrderDO.ORDER_ID,orderId));
    }
}
