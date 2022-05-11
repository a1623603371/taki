package com.taki.inventory.service.impl;

import com.taki.common.redis.RedisCache;
import com.taki.inventory.cache.CacheSupport;
import com.taki.inventory.dao.ProductStockDao;
import com.taki.inventory.domain.entity.ProductStockDO;
import com.taki.inventory.domain.request.AddProductStockRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

/**
 * @ClassName AddProductStockProcessor
 * @Description 添加商品库存处理器
 * @Author Long
 * @Date 2022/5/11 15:35
 * @Version 1.0
 */

@Slf4j
@Component
public class AddProductStockProcessor {

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ProductStockDao productStockDao;



    /** 
     * @description: 执行添加 商品库存 逻辑
     * @param addProductStockRequest
     * @return  void
     * @author Long
     * @date: 2022/5/11 15:48
     */ 
    @Transactional
    public void doAdd(AddProductStockRequest addProductStockRequest){
        //1. 构造商品库存
        ProductStockDO productStockDO = buildProductStock(addProductStockRequest);

        //2 . 保存商品库存到MYSQL
        productStockDao.save(productStockDO);

        //3.保存商品库存到redis
        addStockToRedis(productStockDO);
    }

    /** 
     * @description: 添加 库存到redis
     * @param productStockDO
     * @return  void
     * @author Long
     * @date: 2022/5/11 16:45
     */ 
    public void addStockToRedis(ProductStockDO productStockDO){
        String productStockKey = CacheSupport.buildProductStockKey(productStockDO.getSkuCode());

        Map<String,String> productStockValue =CacheSupport.buildProductStockValue(productStockDO.getSaleStockQuantity(), productStockDO.getSaleStockQuantity());

        redisCache.hPutAll(productStockKey,productStockValue);
    }


    /** 
     * @description:
     * @param addProductStockRequest 执行添加 商品库存 请求
     * @return
     * @author Long
     * @date: 2022/5/11 16:24
     */ 
    private ProductStockDO buildProductStock(AddProductStockRequest addProductStockRequest) {

        ProductStockDO productStockDO = new ProductStockDO();

        productStockDO.setSkuCode(addProductStockRequest.getSkuCode());
        productStockDO.setSaledStockQuantity(0L);

        return productStockDO;

    }
}
