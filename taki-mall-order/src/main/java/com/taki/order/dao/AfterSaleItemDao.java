package com.taki.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.order.domain.entity.AfterSaleItemDO;
import com.taki.order.domain.entity.OrderAmountDO;
import com.taki.order.mapper.AfterSaleItemMapper;
import com.taki.order.mapper.OrderAmountMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName AfterSaleItemDao
 * @Description 售后条目 DAO 组件
 * @Author Long
 * @Date 2022/3/11 15:18
 * @Version 1.0
 */
@Repository
public class AfterSaleItemDao extends BaseDAO<AfterSaleItemMapper, AfterSaleItemDO> {


    /**
     * @description: 根据 订单id 和 sku 编码 查询 售后单条目
     * @param orderId 订单id
     * @param skuCode sku 编码
     * @return  售后单条目
     * @author Long
     * @date: 2022/4/3 20:38
     */
    public List<AfterSaleItemDO> getOrderIdAndSkuCode(String orderId, String skuCode) {

        return this.list(new QueryWrapper<AfterSaleItemDO>().eq(AfterSaleItemDO.ORDER_ID,orderId).eq(AfterSaleItemDO.SKU_CODE,skuCode));

    }

    /**
     * @description: 根据 订单id查询 售后单条目
     * @param orderId 订单Id
     * @return
     * @author Long
     * @date: 2022/4/3 20:38
     */
    public List<AfterSaleItemDO> listByOrderId(String orderId) {
        return this.list(new QueryWrapper<AfterSaleItemDO>().eq(AfterSaleItemDO.ORDER_ID,orderId));
    }

    /**
     * @description: 根据售后单Id 查询 售后条目信息
     * @param afterSaleId 售后单Id
     * @return  售后条目信息 集合
     * @author Long
     * @date: 2022/4/4 22:57
     */
    public List<AfterSaleItemDO> listByAfterSaleId(String afterSaleId) {
        return this.list(new QueryWrapper<AfterSaleItemDO>().eq(AfterSaleItemDO.AFTER_SALE_ID,afterSaleId));
    }

    /**
     * @description: 根据订单Id 和 售后id查询售后条目
     * @param orderId 订单Id
     *
     * @param afterSaleId 售后单Id
     * @return
     * @author Long
     * @date: 2022/5/19 20:59
     */
    public AfterSaleItemDO getOrderIdAndAfterSaleId(String orderId, String afterSaleId) {

        return this.getOne(new QueryWrapper<AfterSaleItemDO>()
                .eq(AfterSaleItemDO.ORDER_ID,orderId).eq(AfterSaleItemDO.AFTER_SALE_ID,afterSaleId));
    }
    /**
     * @description: 根据订单Id 和 售后id查询售后条目集合
     * @param orderId 订单Id
     * @param afterSaleId 售后单Id
     * @return
     * @author Long
     * @date: 2022/5/19 20:59
     */
    public List<AfterSaleItemDO> listNotContainCurrentAfterSaleId(String orderId, String afterSaleId) {
        return this.list(new QueryWrapper<AfterSaleItemDO>()
                .eq(AfterSaleItemDO.ORDER_ID,orderId).eq(AfterSaleItemDO.AFTER_SALE_ID,afterSaleId));
    }
}
