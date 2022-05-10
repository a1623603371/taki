package com.taki.inventory.cache;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CacheSupport
 * @Description 库存缓存
 * @Author Long
 * @Date 2022/5/10 18:34
 * @Version 1.0
 */
public interface CacheSupport {

    /**
     *商品库存
     */
    String PREFIX_PRODUCT_STOCK = "PRODUCT_STOCK:";

    /**
     * 销售库存key
     */
    String SALE_STOCK = "saleStock";


    /**
     *构造器缓存商品 库存key
     */
    String  SALED_STOCK = "saledStock";


    /**
     * 构造器 商品缓存库存key
     * @param skuCode
     * @return
     */
    static String buildProductStockKey(String  skuCode){

        return PREFIX_PRODUCT_STOCK + skuCode;
    }



    /** 
     * @description: 构造器缓存商品 value
     * @param 
     * @return
     * @author Long
     * @date: 2022/5/10 18:39
     */ 
    static Map<String,String> buildProductStockValue(Long  saleStockQuantity,Long saledStockQuantity){

        Map<String,String> value = new HashMap<>();

        value.put(SALE_STOCK,String.valueOf(saleStockQuantity));
        value.put(SALED_STOCK,String.valueOf(saledStockQuantity));

        return value;
        
    }
}

