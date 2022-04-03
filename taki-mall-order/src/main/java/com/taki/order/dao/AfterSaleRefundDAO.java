package com.taki.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.order.domain.entity.AfterSaleRefundDO;
import com.taki.order.mapper.AfterSaleRefundMapper;
import org.springframework.stereotype.Repository;

/**
 * @ClassName AfterSaleRefundDAO
 * @Description 售后支付 DAO 组件
 * @Author Long
 * @Date 2022/3/11 17:38
 * @Version 1.0
 */
@Repository
public class AfterSaleRefundDAO extends BaseDAO<AfterSaleRefundMapper, AfterSaleRefundDO> {



    public AfterSaleRefundDO getByAfterSaleId(Long afterSaleId) {
        return this.getOne(new QueryWrapper<AfterSaleRefundDO>().eq(AfterSaleRefundDO.AFTER_SALE_ID,afterSaleId));
    }
}
