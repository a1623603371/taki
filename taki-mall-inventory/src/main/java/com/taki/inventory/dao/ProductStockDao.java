package com.taki.inventory.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.inventory.domain.entity.ProductStockDO;
import com.taki.inventory.mapper.ProductStockMapper;
import org.apache.ibatis.annotations.Param;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @ClassName ProductStockDao
 * @Description 商品库存 DAO 组件
 * @Author Long
 * @Date 2022/2/16 17:19
 * @Version 1.0
 */
@Repository
public class ProductStockDao extends BaseDAO<ProductStockMapper, ProductStockDO> {
    
   @Autowired
  private ProductStockMapper productStockMapper;

    /**
     * @description: 根据商品SKU编码查询 商品库存
     * @param skuCode 商品Sku编码
     * @return  商品库存
     * @author Long
     * @date: 2022/2/17 9:59
     */
  public ProductStockDO getBySkuCode(String skuCode){

      return this.getOne(new QueryWrapper<ProductStockDO>().eq(ProductStockDO.SKU_CODE,skuCode));

  }

    /**
     * @description: 锁定库存SQL
     * @param skuCode 商品SKU 编码
     * @param saleQuantity 销售数量
     * @return  响应结果
     * @author Long
     * @date: 2022/2/17 9:39
     */
  public Boolean lockProductStock(String skuCode,Integer saleQuantity){

    return productStockMapper.lockProductStock(skuCode,saleQuantity);
  }


    /**
     * @description:  释放锁定库存
     * @param skuCode 商品SKU编码
     * @param saleQuantity 释放销售数量
     * @return  结果
     * @author Long
     * @date: 2022/2/17 10:06
     */
   public  Boolean releaseProductStock( String skuCode,Integer saleQuantity){
        return productStockMapper.releaseProductStock(skuCode,saleQuantity);
    }

    /** 
     * @description:  扣减销售库存
     * @param skuCode 商品 sku 编码
     * @param saleQuantity 销售数量
     * @param originSaleStock 当前销售库存
     * @return  int
     * @author Long
     * @date: 2022/5/12 15:29
     */ 
    public Boolean deductSaleStock(String skuCode, Integer saleQuantity, Integer originSaleStock) {

        return productStockMapper.deductSaleStock(skuCode,saleQuantity,originSaleStock);
    }

    /**
     * @description:  增加已销售库存
     * @param skuCode 商品 sku 编码
     * @param  saleQuantity 销售库存
     * @param originSaledQuantity 当前 已销售库存
     * @return  void
     * @author Long
     * @date: 2022/5/12 15:57
     */
    public Boolean increaseSaledStock(String skuCode, Integer saleQuantity, Integer originSaledQuantity) {

        return productStockMapper.increaseSaledStock(skuCode,saleQuantity,originSaledQuantity);
    }

    /**
     * @description:  还原销售库存
     * @param skuCode 商品 sku 编码
     * @param  saleQuantity 销售库存
     * @param originSaleStock 当前 销售库存
     * @return  void
     * @author Long
     * @date: 2022/5/12 15:57
     */
    public Boolean restoreSaleStock(String skuCode, Integer saleQuantity, int originSaleStock) {

        return productStockMapper.restoreSaleStock(skuCode,saleQuantity,originSaleStock);
    }
}
