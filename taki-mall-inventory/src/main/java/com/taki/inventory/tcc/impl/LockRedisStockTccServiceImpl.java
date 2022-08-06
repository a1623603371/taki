package com.taki.inventory.tcc.impl;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.redis.RedisCache;
import com.taki.common.utli.LoggerFormat;
import com.taki.common.utli.MdcUtil;
import com.taki.inventory.cache.CacheSupport;
import com.taki.inventory.cache.LuaScript;
import com.taki.inventory.domain.dto.DeductStockDTO;
import com.taki.inventory.tcc.LockRedisStockTccService;
import com.taki.inventory.tcc.TccResultHolder;
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
        Integer saleQuantity = deductStock.getSaleQuantity().intValue();
        // 标识try阶段
        TccResultHolder.tagTryStart(getClass(),skuCode,xid);

        log.info(LoggerFormat
                .build()
                .remark("一阶段方法:扣减redis销售库存")
                .data("deductStock",saleQuantity)
                .data("xid",xid)
                .finish());
        // 空悬挂
        if (isEmptyRollback()){
            return false;
        }

        String luaScript = LuaScript.DEDUCT_SALE_STOCK;
        String saleStockKey = CacheSupport.SALE_STOCK;
        String productStockKey = CacheSupport.buildProductStockKey(skuCode);

        Long result = redisCache.execute(new DefaultRedisScript<>(luaScript,Long.class),
                Arrays.asList(productStockKey,saleStockKey),String.valueOf(saleQuantity));

        // 标识try 成功
        if (result >  0){
            TccResultHolder.tagTrySuccess(getClass(),skuCode,xid);
        }


        return result > 0;
    }



    @Override
    public void commit(BusinessActionContext actionContext) {
        String xid = actionContext.getXid();
        DeductStockDTO deductStock = ((JSONObject)actionContext.getActionContext("deductStock")).toJavaObject(DeductStockDTO.class);

        String traceId = (String) actionContext.getActionContext().get("traceId");
        MdcUtil.setUserTraceId(traceId);

        String skuCode = deductStock.getSkuCode();
        Integer saleQuantity = deductStock.getSaleQuantity().intValue();

        log.info(LoggerFormat.build()
                .remark("二阶段方法：增加redis已销售库存")
                .data("deductStock",deductStock)
                .data("xid",xid)
                .finish());

        // 幂等

        if (!TccResultHolder.isTrySuccess(getClass(),skuCode,xid)){

            log.info(LoggerFormat.build()
                    .remark("已执行过commit 阶段")
                    .data("deductStock",deductStock)
                    .data("xid",xid)
                    .finish());
            return;
        }

        String luaScript = LuaScript.INCREASE_SALED_STOCK;
        String saledStockKey = CacheSupport.SALED_STOCK;

        String productStockKey =CacheSupport.buildProductStockKey(skuCode);

        Long result =  redisCache.execute(new DefaultRedisScript<>(luaScript,Long.class),Arrays.asList(productStockKey,saledStockKey),String.valueOf(saleQuantity));

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
        String traceId = (String) actionContext.getActionContext().get("traceId");
        MdcUtil.setUserTraceId(traceId);

        Integer saleQuantity = deductStock.getSaleQuantity().intValue();


        log.info(LoggerFormat.build()
                .remark("回滚：增加redis已销售库存")
                .data("deductStock",deductStock)
                .data("xid",xid)
                .finish());


        // 空回滚
        if (TccResultHolder.isTrgNull(getClass(),skuCode,xid)){

            log.info(LoggerFormat.build()
                    .remark("redis:: 空回滚")
                    .data("deductStock",deductStock)
                    .data("xid",xid)
                    .finish());
            insertEmptyRollbackTag();
            return;
        }

        // 幂等
        if (!TccResultHolder.isTrySuccess(getClass(),skuCode,xid)){
            log.info(LoggerFormat.build()
                    .remark("redis: 重复回滚")
                    .data("deductStock",deductStock)
                    .data("xid",xid)
                    .finish());
            return;
        }

        String luaScript = LuaScript.RESTORE_SALE_STOCK;
        String productStockKey = CacheSupport.buildProductStockKey(skuCode);
        String saleStockKey = CacheSupport.SALE_STOCK;

        Long result =   redisCache.execute(new DefaultRedisScript<>(luaScript,Long.class),Arrays.asList(productStockKey,saleStockKey),String.valueOf(saleQuantity));


        if (result > 0){
            log.info(LoggerFormat.build()
                    .remark("执行完毕会滚脚本")
                    .data("saleQuantity:",saleQuantity)
                    .finish());
            TccResultHolder.removeResult(getClass(),skuCode,xid);
        }

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
}
