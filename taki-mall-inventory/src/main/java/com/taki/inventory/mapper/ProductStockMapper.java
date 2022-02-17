package com.taki.inventory.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.inventory.domain.entity.ProductStockDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @ClassName ProductStockMapper
 * @Description 商品库存 Mapper 组件
 * @Author Long
 * @Date 2022/2/16 17:19
 * @Version 1.0
 */
@Mapper
public interface ProductStockMapper extends BaseMapper<ProductStockDO> {

    /**
     * @description: 锁定库存SQL
     * @param skuCode 商品SKU 编码
     * @param saleQuantity 销售数量
     * @return  响应结果
     * @author Long
     * @date: 2022/2/17 9:39
     */
    @Update("UPDATE inventory_product_stock SET sale_stock_quantity = sale_stock_quantity - #{saleQuantity}, " +
            "locked_stock_quantity + #{saleQuantity}  WHERE  sku_code = #{skuCode} AND sale_stock_qantity >= #{saleQuantity}")
    Boolean lockProductStock(@Param("skuCode") String skuCode,@Param("saleQuantity") Integer saleQuantity);

    /**
     * @description:  释放锁定库存
     * @param skuCode 商品SKU编码
     * @param saleQuantity 释放销售数量
     * @return  结果
     * @author Long
     * @date: 2022/2/17 10:06
     */
    @Update("UPDATE inventory_product_stock SET sale_stock_quantity = sale_stock_quantity + #{saleQuantity}, " +
            "locked_stock_quantity - #{saleQuantity}  WHERE  sku_code = #{skuCode} AND sale_stock_qantity >= #{saleQuantity}")
    Boolean releaseProductStock(@Param("skuCode") String skuCode,@Param("saleQuantity") Integer saleQuantity);


}
