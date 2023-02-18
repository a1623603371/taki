package com.taki.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.dao.BaseDAO;
import com.taki.order.domain.entity.AfterSaleInfoDO;
import com.taki.order.domain.entity.AfterSaleRefundDO;
import com.taki.order.mapper.AfterSaleRefundMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName AfterSaleRefundDAO
 * @Description 售后支付 DAO 组件
 * @Author Long
 * @Date 2022/3/11 17:38
 * @Version 1.0
 */
@Repository
public class AfterSaleRefundDAO extends BaseDAO<AfterSaleRefundMapper, AfterSaleRefundDO> {



    public AfterSaleRefundDO getByAfterSaleId(String afterSaleId) {
        return this.getOne(new QueryWrapper<AfterSaleRefundDO>().eq(AfterSaleRefundDO.AFTER_SALE_ID,afterSaleId));
    }

    /**
     * @description: 根据 售后单Id 查询 售后退款信息集合
     * @param afterSaleId
     * @return 售后退款信息集合
     * @author Long
     * @date: 2022/4/4 23:01
     */
    public List<AfterSaleRefundDO> listByAfterSaleId(Long afterSaleId) {
            return this.list(new QueryWrapper<AfterSaleRefundDO>().eq(AfterSaleInfoDO.AFTER_SALE_ID,afterSaleId));

    }

    /**
     * @description: 更新 售后单 状态
     * @param afterSaleRefundDO 售后单
     * @return  void
     * @author Long
     * @date: 2022/4/6 18:18
     */
    public Boolean updateAfterSaleRefundStatus(AfterSaleRefundDO afterSaleRefundDO) {

        return this.update(afterSaleRefundDO,new QueryWrapper<AfterSaleRefundDO>().eq(AfterSaleRefundDO.AFTER_SALE_ID,afterSaleRefundDO.getAfterSaleId()));
    }

    /** 
     * @description:  查询 售后退款单
     * @param afterSaleId 售后单Id
     * @return
     * @author Long
     * @date: 2022/6/9 19:19
     */ 
    public AfterSaleRefundDO findAfterSaleRefundByfterSaleId(Long afterSaleId) {
        return this.getOne(new QueryWrapper<AfterSaleRefundDO>().eq(AfterSaleRefundDO.AFTER_SALE_ID,afterSaleId));
    }
}
