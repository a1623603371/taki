package com.taki.inventory.tcc.impl;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.redis.RedisCache;
import com.taki.inventory.cache.CacheSupport;
import com.taki.inventory.cache.LuaScript;
import com.taki.inventory.domain.dto.DeductStockDTO;
import com.taki.inventory.tcc.LockRedisStockTccService;
import com.taki.inventory.tcc.TccResultHolder;
import io.seata.rm.DefaultResourceManager;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;

/**
 * @ClassName LockRedisStockTccServiceImpl
 * @Description Redis TCC
 * @Author Long
 * @Date 2022/5/12 17:04
 * @Version 1.0
 */
@Service
@Slf4j
public class LockRedisStockTccServiceImpl implements LockRedisStockTccService {


    @Autowired
    private RedisCache redisCache;

    @Override
    public boolean deductStock(BusinessActionContext actionContext, DeductStockDTO deductStock) {
        String xid = actionContext.getXid();
        String skuCode = deductStock.getSkuCode();
        Integer saleQuantity = deductStock.getSaleQuantity();
        Integer originSaleStock = deductStock.getOriginSaleStock();
        Integer originSaledStock = deductStock.getOriginSaledStock();

        // 标识try阶段
        TccResultHolder.tagTryStart(getClass(),skuCode,xid);

        log.info("一阶段方法:扣减redis销售库存，deductStock={},xid={}", JSONObject.toJSONString(deductStock),xid);

        // 空悬挂
        if (isEmptyRollback()){
            return false;
        }

        String luaScript = LuaScript.DEDUCT_SALE_STOCK;
        String saleStockKey = CacheSupport.SALE_STOCK;
        String productStockKey = CacheSupport.PREFIX_PRODUCT_STOCK;

        Long result = redisCache.execute(new DefaultRedisScript<>(luaScript,Long.class),
                Arrays.asList(productStockKey,saleStockKey),String.valueOf(saleQuantity),String.valueOf(originSaleStock));

        // 标识try 成功
        if (result >  0){
            TccResultHolder.tagTrySuccess(getClass(),skuCode,xid);
        }



        return result > 0;
    }

    /**
     * @description: 构造扣减库存日志
     * @param
     * @return  void
     * @author Long
     * @date: 2022/5/12 16:21
     */
    private void insertEmptyRollbackTag() {

        // 在数据库 插入 空回滚标识
    }

    private boolean isEmptyRollback() {
        // 需要查询本地数据库，看是否发生了空回滚
        return false;
    }

    @Override
    public void commit(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        DeductStockDTO deductStock = ((JSONObject)actionContext.getActionContext("deductStock")).toJavaObject(DeductStockDTO.class);
        String skuCode = deductStock.getSkuCode();
        Integer saleQuantity = deductStock.getSaleQuantity();
        Integer originSaleStock = deductStock.getOriginSaleStock();
        Integer originSaledStock = deductStock.getOriginSaledStock();
        log.info("二阶段方法：增加redis已销售库存，deductStock={},xid={}",JSONObject.toJSONString(deductStock),xid);

        // 幂等

        if (!TccResultHolder.isTrySuccess(getClass(),skuCode,xid)){
            log.info("已执行过commit 阶段");
            return;
        }

        String luaScript = LuaScript.INCREASE_SALED_STOCK;
        String saledStockKey = CacheSupport.SALED_STOCK;

        String prodcutStockKey =CacheSupport.buildProductStockKey(skuCode);

        Long result =  redisCache.execute(new DefaultRedisScript<>(luaScript,Long.class),Arrays.asList(prodcutStockKey,saledStockKey),String.valueOf(saleQuantity),String.valueOf(originSaledStock));

        if (result > 0){
            // 移除标识
            TccResultHolder.removeResult(getClass(),skuCode,xid);
        }

    }

    @Override
    public void rollback(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        DeductStockDTO deductStock = ((JSONObject)actionContext.getActionContext("deductStock")).toJavaObject(DeductStockDTO.class);
        String skuCode = deductStock.getSkuCode();
        Integer saleQuantity = deductStock.getSaleQuantity();
        Integer originSaleStock = deductStock.getOriginSaleStock();
        Integer originSaledStock = deductStock.getOriginSaledStock();

        log.info("回滚：增加redis已销售库存，deductStock={},xid={}",JSONObject.toJSONString(deductStock),xid);


        // 空回滚
        if (TccResultHolder.isTrgNull(getClass(),skuCode,xid)){
            log.info("redis:: 空回滚");
            insertEmptyRollbackTag();
            return;
        }

        // 幂等
        if (TccResultHolder.isTrySuccess(getClass(),skuCode,xid)){
            log.info("redis: 重复回滚");
            return;
        }

        String luaScript = LuaScript.RELEASE_PRODUCT_STOCK;
        String productStockKey = CacheSupport.buildProductStockKey(skuCode);
        String saleStockKey = CacheSupport.SALE_STOCK;

        Long result =   redisCache.execute(new DefaultRedisScript<>(luaScript,Long.class),Arrays.asList(productStockKey,saleStockKey),String.valueOf(saleStockKey),String.valueOf(originSaleStock - saleQuantity));

        if (result > 0){

            TccResultHolder.removeResult(getClass(),skuCode,xid);
        }

    }
}
