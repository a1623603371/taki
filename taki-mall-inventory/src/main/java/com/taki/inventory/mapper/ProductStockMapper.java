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

    @Update("UPDATE inventory_product_stock set sale_stock_quantity = sale_stock_quantity - #{saleQuantity}")
    int lockProductStock(@Param("skuCode") String skuCode,@Param("saleQuantity") Integer saleQuantity);




}
