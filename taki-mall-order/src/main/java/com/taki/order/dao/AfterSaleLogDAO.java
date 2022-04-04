package com.taki.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.order.domain.entity.AfterSaleItemDO;
import com.taki.order.domain.entity.AfterSaleLogDO;
import com.taki.order.mapper.AfterSaleItemMapper;
import com.taki.order.mapper.AfterSaleLogMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName AfterSaleLogDAO
 * @Description 售后变更 DAO 组件
 * @Author Long
 * @Date 2022/4/4 14:49
 * @Version 1.0
 */
@Repository
public class AfterSaleLogDAO extends BaseDAO<AfterSaleLogMapper, AfterSaleLogDO> {

    /**
     * @description: 根据 售后单Id 查询 售后日志信息集合
     * @param afterSaleId 售后单Id
     * @return 售后日志信息集合
     * @author Long
     * @date: 2022/4/4 23:31
     */
    public List<AfterSaleLogDO> listByAfterSaleId(Long afterSaleId) {

        return this.list(new QueryWrapper<AfterSaleLogDO>().eq(AfterSaleLogDO.AFTER_SALE_ID,afterSaleId));
    }
}
