package com.taki.inventory.service.impl;

import com.taki.common.redis.RedisCache;
import com.taki.inventory.cache.CacheSupport;
import com.taki.inventory.dao.ProductStockDao;
import com.taki.inventory.domain.entity.ProductStockDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

/**
 * @ClassName SyncStockToCacheProcessor
 * @Description 同步商品sku库存 到缓存 处理器
 * @Author Long
 * @Date 2022/6/20 14:37
 * @Version 1.0
 */
@Slf4j
@Component
public class SyncStockToCacheProcessor {

    @Autowired
    private ProductStockDao productStockDao;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private AddProductStockProcessor addProductStockProcessor;



    /**
     * @description: 执行扣减商品库存逻辑
     * @param skuCode
     * @return  void
     * @author Long
     * @date: 2022/6/20 14:39
     */
    public void doSync(String  skuCode){
        //1.查询商品库存
        ProductStockDO productStock = productStockDao.getBySkuCode(skuCode);

        if (!ObjectUtils.isEmpty(productStock)){

            //2.删除缓存数据
            redisCache.delete(CacheSupport.buildProductStockKey(productStock.getSkuCode()));
            //3. 保存商品存到 redis
            addProductStockProcessor.addStockToRedis(productStock);
        }

    }
}
