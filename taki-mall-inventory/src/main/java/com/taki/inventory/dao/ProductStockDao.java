package com.taki.inventory.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.dao.BaseDAO;
import com.taki.inventory.domain.entity.ProductStockDO;
import com.taki.inventory.mapper.ProductStockMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

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

     * @return  int
     * @author Long
     * @date: 2022/5/12 15:29
     */ 
    public Boolean deductSaleStock(String skuCode, Integer saleQuantity) {

        return productStockMapper.deductSaleStock(skuCode,saleQuantity);
    }

    /**
     * @description:  增加已销售库存
     * @param skuCode 商品 sku 编码
     * @param  saleQuantity 销售库存
     * @return  void
     * @author Long
     * @date: 2022/5/12 15:57
     */
    public Boolean increaseSaledStock(String skuCode, Integer saleQuantity) {

        return productStockMapper.increaseSaledStock(skuCode,saleQuantity);
    }

    /**
     * @description:  还原销售库存
     * @param skuCode 商品 sku 编码
     * @param  saleQuantity 销售库存
     * @return  void
     * @author Long
     * @date: 2022/5/12 15:57
     */
    public Boolean restoreSaleStock(String skuCode, Long saleQuantity) {

        return productStockMapper.restoreSaleStock(skuCode,saleQuantity.intValue());
    }

    /**
     * @description:  调整 商品库存
     * @param skuCode 商品sku 编码
     * @param originSaleStockQuantity 当前 销售库存数量
     * @param  stockIncremental 库存 调整数量
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/5/13 19:05
     */
    public Boolean modifyProductStock(String skuCode, Long originSaleStockQuantity, Long stockIncremental) {

       return productStockMapper.modifyProductStock(skuCode,originSaleStockQuantity,stockIncremental);
    }

    /** 
     * @description:  初始化压测库存数据
     * @param skuCodes
     * @return  void
     * @author Long
     * @date: 2022/6/20 20:14
     */ 
    public void initMeasureInventoryData(List<String> skuCodes) {
        this.update().set(ProductStockDO.SALE_STOCK_QUANTITY,100000000000L)
                        .set(ProductStockDO.SALED_STOCK_QUANTITY,0L).in(ProductStockDO.SKU_CODE,skuCodes).update();
    }
}
