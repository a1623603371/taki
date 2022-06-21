package com.taki.inventory.service.impl;

import com.taki.common.redis.RedisCache;
import com.taki.inventory.cache.CacheSupport;
import com.taki.inventory.cache.LuaScript;
import com.taki.inventory.dao.ProductStockDao;
import com.taki.inventory.dao.ProductStockLogDao;
import com.taki.inventory.domain.entity.ProductStockLogDO;
import com.taki.inventory.enums.StockLogStatusEnum;
import com.taki.inventory.exception.InventoryBizException;
import com.taki.inventory.exception.InventoryErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;

/**
 * @ClassName ReleaseProductStockProcessor
 * @Description 释放商品库存处理器
 * @Author Long
 * @Date 2022/6/20 16:01
 * @Version 1.0
 */
@Slf4j
@Component
public class ReleaseProductStockProcessor {


    @Autowired
    private ProductStockDao productStockDao;

    @Autowired
    private ProductStockLogDao productStockLogDao;

    @Autowired
    private RedisCache redisCache;


    @Transactional(rollbackFor = Exception.class)
    public  void doRelease(String  orderId, String skuCode, Integer saleQuantity, ProductStockLogDO productStockLogDO){
        //1.执行mysql 释放商品库存逻辑
        Boolean releaseResult= productStockDao.releaseProductStock(skuCode,saleQuantity);

        if (!releaseResult){
            throw new InventoryBizException(InventoryErrorCodeEnum.RELEASE_PRODUCT_SKU_STOCK_ERROR);
        }

        //2.更新 库存日志的状态为"已释放'

        if (!ObjectUtils.isEmpty(productStockLogDO)){
            productStockLogDao.updateStatus(productStockLogDO.getId(), StockLogStatusEnum.RELEASE);
        }
        //3. 执行redis 库存释放逻辑
        String luaScript = LuaScript.RELEASE_PRODUCT_STOCK;
        String saleStockKey = CacheSupport.SALE_STOCK;
        String saledStockKey = CacheSupport.SALED_STOCK;

        String productStockKey = CacheSupport.buildProductStockKey(skuCode);

        Long result = redisCache.execute(new DefaultRedisScript<>(luaScript,Long.class),
                Arrays.asList(productStockKey,saleStockKey,saledStockKey),String.valueOf(saleQuantity));

        if (result < 0){
            throw new InventoryBizException(InventoryErrorCodeEnum.RELEASE_PRODUCT_SKU_STOCK_ERROR);
        }

    }
}
