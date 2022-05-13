package com.taki.inventory.service.impl;

import com.taki.common.redis.RedisCache;
import com.taki.inventory.cache.CacheSupport;
import com.taki.inventory.cache.LuaScript;
import com.taki.inventory.dao.ProductStockDao;
import com.taki.inventory.domain.entity.ProductStockDO;
import com.taki.inventory.domain.request.ModifyProductStockRequest;
import com.taki.inventory.exception.InventoryBizException;
import com.taki.inventory.exception.InventoryErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

/**
 * @ClassName ModifyProductStockProcessor
 * @Description 调整商品库存处理器
 * @Author Long
 * @Date 2022/5/13 18:54
 * @Version 1.0
 */
@Component
@Slf4j
public class ModifyProductStockProcessor {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ProductStockDao productStockDao;


    /**
     * 执行 调整商品库存
     * @param productStock 商品库存
     * @param stockIncremental 库存增量
     */
    @Transactional(rollbackFor = Exception.class)
    public void doModify(ProductStockDO productStock ,Long  stockIncremental){

        //1.更新mysql 商品 可销售库存数量
        String skuCode = productStock.getSkuCode();

        Long originSaleStockQuantity = productStock.getSaleStockQuantity();

        Boolean result = productStockDao.modifyProductStock(skuCode,originSaleStockQuantity,stockIncremental);

        if (!result){
            throw new InventoryBizException(InventoryErrorCodeEnum.MODIFY_PRODUCT_SKU_STOCK_ERROR);
        }

        // 执行lua 脚本

        String luaScript = LuaScript.MODIFY_PRODUCT_STOCK;

        String productStockKey = CacheSupport.buildProductStockKey(skuCode);

        String saleStockKey = CacheSupport.SALE_STOCK;

        Long    redisResult = redisCache.execute(new DefaultRedisScript<>(luaScript,Long.class),Arrays.asList(productStockKey,saleStockKey),String.valueOf(stockIncremental));

        if (redisResult < 0){
            throw new InventoryBizException(InventoryErrorCodeEnum.MODIFY_PRODUCT_SKU_STOCK_ERROR);
        }

    }
}
