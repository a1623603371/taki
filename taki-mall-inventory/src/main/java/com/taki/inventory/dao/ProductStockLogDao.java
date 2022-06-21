package com.taki.inventory.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.inventory.domain.entity.ProductStockDO;
import com.taki.inventory.domain.entity.ProductStockLogDO;
import com.taki.inventory.enums.StockLogStatusEnum;
import com.taki.inventory.mapper.ProductStockLogMapper;
import org.springframework.stereotype.Repository;

/**
 * @ClassName ProductStockLogDao
 * @Description 商品库存日志DAO 组件
 * @Author Long
 * @Date 2022/5/11 20:57
 * @Version 1.0
 */
@Repository
public class ProductStockLogDao extends BaseDAO<ProductStockLogMapper, ProductStockLogDO> {




    /**
     * @description: 根据 订单ID 和 skuCode 查询 商品库存日志
     * @param orderId 订单Id
     * @param skuCode
     * @return  商品库存日志
     * @author Long
     * @date: 2022/5/11 21:11
     */
    public ProductStockLogDO getLog(String orderId, String skuCode) {

        return this.getOne(new QueryWrapper<ProductStockLogDO>().eq(ProductStockLogDO.ORDER_ID,orderId).eq(ProductStockLogDO.SKU_CODE,skuCode)) ;

    }

    
    /** 
     * @description:  查询sku库存最近一笔扣减日志
     * @param skuCode
     * @return
     * @author Long
     * @date: 2022/6/20 13:47
     */ 
    public ProductStockLogDO getLatestOne(String skuCode) {

        return this.getOne(new QueryWrapper<ProductStockLogDO>()
                .eq(ProductStockLogDO.SKU_CODE,skuCode)
                .orderByDesc(ProductStockLogDO.ID).last("limit 1"));
    }

    /**
     * @description: 修改库存日志状态
     * @param id
     * @paramrelease
     * @return  void
     * @author Long
     * @date: 2022/6/20 16:14
     */
    public Boolean updateStatus(Long id, StockLogStatusEnum release) {

        return this.update().set(ProductStockLogDO.STATUS,release.getCode()).eq(ProductStockLogDO.ID,id).update();
    }
}
