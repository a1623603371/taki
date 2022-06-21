package com.taki.inventory.tcc.impl;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.redis.RedisLock;
import com.taki.common.utlis.LoggerFormat;
import com.taki.common.utlis.MdcUtil;
import com.taki.inventory.dao.ProductStockDao;
import com.taki.inventory.dao.ProductStockLogDao;
import com.taki.inventory.domain.dto.DeductStockDTO;
import com.taki.inventory.domain.entity.ProductStockDO;
import com.taki.inventory.domain.entity.ProductStockLogDO;
import com.taki.inventory.tcc.LockMysqlStockTccService;
import com.taki.inventory.tcc.TccResultHolder;
import io.seata.rm.tcc.api.BusinessActionContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName LockMysqlStockTccServiceImpl
 * @Description mysql TCC
 * @Author Long
 * @Date 2022/5/12 13:21
 * @Version 1.0
 */
@Service
@Slf4j
public class LockMysqlStockTccServiceImpl implements LockMysqlStockTccService {

    @Autowired
    private ProductStockDao productStockDao;

    @Autowired
    private ProductStockLogDao productStockLogDao;

    @Autowired
    private RedisLock redisLock;


    @Override
    public boolean deductStock(BusinessActionContext actionContext, DeductStockDTO deductStock) {
        String xid = actionContext.getXid();
        String skuCode = deductStock.getSkuCode();
        Integer saleQuantity = deductStock.getSaleQuantity().intValue();
        // 标识try阶段开始执行
        TccResultHolder.tagTryStart(getClass(),skuCode,xid);

        // 空悬挂问题：rollback 接口比 try 接口 先执行，即 rollback 接口进行 了空回滚，try 阶段 接口才执行，导致 try接口 预留的资源无法被取消
        //解决空悬挂问题的思路： 当 rollback 接口 出现 空回滚时，需要打一个标识（在数据库中查询一条记录），在try 阶段判断
        if (isEmptyRollback()){

            return false;
        }
        log.info(LoggerFormat
                .build()
                .remark("一阶段方法:扣减mysql销售库存")
                .data("deductStock",saleQuantity)
                .data("xid",xid)
                .finish());
        Boolean result = productStockDao.deductSaleStock(skuCode,saleQuantity);

        if (result){
            ProductStockLogDO logDO =  buildStockLog(deductStock);
            // 2.插入一条扣减日志表
            log.info(LoggerFormat
                    .build()
                    .remark("插入一条扣减日志记录")
                    .data("logDO",logDO)
                    .data("xid",xid)
                    .finish());
            productStockLogDao.save(logDO);
            TccResultHolder.tagTrySuccess(getClass(),skuCode,xid);
        }
        return result;
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
    Integer saleQuantity = deductStock.getSaleQuantity().intValue();


     String traceId = (String) actionContext.getActionContext("traceId");
    MdcUtil.setTraceId(traceId);


    log.info(",deductStock,xid = {}",JSONObject.toJSONString(deductStock),xid);

        log.info(LoggerFormat
                .build()
                .remark("二阶段提交：增加mysql 已销售库存")
                .data("deductStock",deductStock)
                .data("xid",xid)
                .finish());

    // 幂等
    //当出现网络异常 后或者 TC Server 异常 ，回 出现 重复调用 commit阶段 情况，所以需要进行幂等操作
        if (!TccResultHolder.isTrySuccess(getClass(),skuCode,xid)){
            return;
        }
        // 1.增加已销售库存
        productStockDao.increaseSaledStock(skuCode,saleQuantity);
        //移除标识
        TccResultHolder.removeResult(getClass(),skuCode,xid);

    }
    /**
     * @description: 构造 商品库存 日志
     * @param deductStock 扣减库存 数据
     * @return
     * @author Long
     * @date: 2022/5/12 16:09
     */
    private ProductStockLogDO buildStockLog(DeductStockDTO deductStock) {

        ProductStockDO productStock = deductStock.getProductStockDO();

        Integer saleQuantity =deductStock.getSaleQuantity().intValue();
        Long originSaleStock = productStock.getSaleStockQuantity();
        Long originSaledStocck = getOriginSaledStock(productStock);


        ProductStockLogDO logDO = new ProductStockLogDO();
        logDO.setOrderId(deductStock.getOrderId());
        logDO.setSkuCode(deductStock.getSkuCode());
        logDO.setOriginSaleStockQuantity(originSaleStock);
        logDO.setOriginSaledStockQuantity(originSaledStocck);
        logDO.setDeductedSaleStockQuantity(originSaleStock - saleQuantity);
        logDO.setIncreasedSaledStockQuantity(originSaleStock + saleQuantity);

        return logDO;



    }
    
    /** 
     * @description: 获取sku的原始已销售库存
     * @param productStock 商品库存数据
     * @return  java.lang.Long
     * @author Long
     * @date: 2022/6/20 13:40
     */ 
    private Long getOriginSaledStock(ProductStockDO productStock) {
        //1.查询sku库存最近一笔扣减日志
        ProductStockLogDO lateStLog = productStockLogDao.getLatestOne(productStock.getSkuCode());

        //2.获取原始的已销售库存
        Long originSaledStock = null;

        if (ObjectUtils.isEmpty(lateStLog)){
            //第一次扣减，直接取productStockDO的saledStockQuantity
            originSaledStock = productStock.getSaledStockQuantity();
        } else {
            //取最近一次扣减日志的increasedSaledStockQuantity
            originSaledStock = lateStLog.getIncreasedSaledStockQuantity();
        }

        return originSaledStock;
    }

    @Override
    public void rollback(BusinessActionContext actionContext) {

        String xid = actionContext.getXid();
        DeductStockDTO deductStock = ((JSONObject)actionContext.getActionContext("deductStock")).toJavaObject(DeductStockDTO.class);
        String skuCode = deductStock.getSkuCode();
        Long saleQuantity = deductStock.getSaleQuantity();

        String traceId = (String) actionContext.getActionContext("traceId");
        MdcUtil.setTraceId(traceId);


        log.info(LoggerFormat
                .build()
                .remark("回滚：增加mysql销售库存")
                .data("deductStock",deductStock)
                .data("xid",xid)
                .finish());

        // 空回滚处理
        //try阶段没有完成的情况下，不必执行回滚，因为try阶段有本地事务，事务失败时已经进行了回滚
        //如果try阶段成功，而其他全局事务参与者失败，这里会执行回滚
        if (TccResultHolder.isTrgNull(getClass(),skuCode,xid)){
            log.info(LoggerFormat
                    .build()
                    .remark("mysql：出现空回滚")
                    .data("deductStock",deductStock)
                    .data("xid",xid)
                    .finish());
        insertEmptyRollbackTag(deductStock);
        return;
        }

        // 幂等 处理
        // try阶段 没有完成的情况下,不执行回滚，因为try阶段有本地事务，事务失败时已经进行了回滚
        //如果 try阶段成功，而且全局事务参与者失败，这里执行回滚

        if (!TccResultHolder.isTrySuccess(getClass(),skuCode,xid)){
            log.info(LoggerFormat
                    .build()
                    .remark("mysql:无需回滚")
                    .data("deductStock",deductStock)
                    .data("xid",xid)
                    .finish());
            return;
        }
        //加锁
        String lockKey = RedisLockKeyConstants.DEDUCT_PRODUCT_STOCK_KEY + skuCode;

        redisLock.lock(lockKey);
        try {

            // 1.还原 销售库存
            productStockDao.restoreSaleStock(skuCode,saleQuantity);
            // 2 插入反向日志
            productStockLogDao.save(buildReverseStock(deductStock));
        }finally {
            redisLock.unLock(lockKey);
        }
    // 移除标识
        TccResultHolder.removeResult(getClass(),skuCode,xid);

    }

    /** 
     * @description: 构建逆向扣减库存日志
     * @param deductStock

     * @author Long
     * @date: 2022/6/20 13:16
     */ 
    private ProductStockLogDO buildReverseStock(DeductStockDTO deductStock) {

        ProductStockDO productStockLogDO = deductStock.getProductStockDO();

        Long saleQuantity = deductStock.getSaleQuantity();

        //查询sku库存最近一笔扣减日志

        ProductStockLogDO latestLog = productStockLogDao.getLatestOne(productStockLogDO.getSkuCode());

        ProductStockLogDO reverseStockLog = new ProductStockLogDO();

        reverseStockLog.setOrderId(deductStock.getOrderId());
        reverseStockLog.setSkuCode(deductStock.getSkuCode());

        reverseStockLog.setOriginSaleStockQuantity(latestLog.getOriginSaleStockQuantity() + saleQuantity);
        reverseStockLog.setOriginSaledStockQuantity(latestLog.getOriginSaledStockQuantity() - saleQuantity);
        reverseStockLog.setDeductedSaleStockQuantity(latestLog.getDeductedSaleStockQuantity() + saleQuantity);
        reverseStockLog.setIncreasedSaledStockQuantity(latestLog.getIncreasedSaledStockQuantity() - saleQuantity);

        return reverseStockLog;

    }

    /** 
     * @description: 构造扣减库存日志
     * @param 
     * @return  void
     * @author Long
     * @date: 2022/5/12 16:21
     */ 
    private void insertEmptyRollbackTag(DeductStockDTO deductStock) {

        // 在数据库 插入 空回滚标识
    }
}
