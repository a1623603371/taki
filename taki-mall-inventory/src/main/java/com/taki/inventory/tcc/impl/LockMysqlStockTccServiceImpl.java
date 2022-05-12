package com.taki.inventory.tcc.impl;

import com.alibaba.fastjson.JSONObject;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
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


    @Override
    public boolean deductStock(BusinessActionContext actionContext, DeductStockDTO deductStock) {
        String xid = actionContext.getXid();
        String skuCode = deductStock.getSkuCode();
        Integer saleQuantity = deductStock.getSaleQuantity();
        Integer originSaleStock = deductStock.getOriginSaleStock();
        Integer originSaledStock = deductStock.getOriginSaledStock();

        // 标识try阶段开始执行
        TccResultHolder.tagTryStart(getClass(),skuCode,xid);

        // 空悬挂问题：rollback 接口比 try 接口 先执行，即 rollback 接口进行 了空回滚，try 阶段 接口才执行，导致 try接口 预留的资源无法被取消
        //解决空悬挂问题的思路： 当 rollback 接口 出现 空回滚时，需要打一个标识（在数据库中查询一条记录），在try 阶段判断
        if (isEmptyRollback()){

            return false;
        }

        log.info("一阶段方法：扣减方法：扣减mysql销售库存，deductStock ={},xid ={}", JSONObject.toJSONString(deductStock),xid);

        Boolean result = productStockDao.deductSaleStock(skuCode,saleQuantity,originSaleStock);


        // 标记 tcc 执行成功
        if (result ){
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
    Integer saleQuantity = deductStock.getSaleQuantity();
    Integer originSaleQuantity = deductStock.getSaleQuantity();
    Integer originSaledQuantity = deductStock.getOriginSaledStock();

    log.info("二阶段提交：增加mysql 已销售库存,deductStock,xid = {}",JSONObject.toJSONString(deductStock),xid);

    // 幂等
    //当出现网络异常 后或者 TC Server 异常 ，回 出现 重复调用 commit阶段 情况，所以需要进行幂等操作
        if (!TccResultHolder.isTrySuccess(getClass(),skuCode,xid)){
            return;
        }

        // 1.增加已销售库存
        productStockDao.increaseSaledStock(skuCode,saleQuantity,originSaledQuantity);

        // 2.插入一条扣减日志表
        log.info("插入一条扣减日志表");
        productStockLogDao.save(buildStockLog(deductStock));

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

        ProductStockLogDO logDO = new ProductStockLogDO();
        logDO.setOrderId(deductStock.getOrderId());
        logDO.setSkuCode(deductStock.getSkuCode());
        logDO.setOriginSaleStockQuantity(deductStock.getOriginSaleStock().longValue());
        logDO.setOriginSaledStockQuantity(deductStock.getOriginSaledStock().longValue());
        logDO.setDeductedSaleStockQuantity(deductStock.getOriginSaleStock().longValue() - deductStock.getSaleQuantity().longValue());
        logDO.setIncreasedSaledStockQuantity(deductStock.getOriginSaledStock().longValue() + deductStock.getSaleQuantity().longValue());

        return logDO;



    }

    @Override
    public void rollback(BusinessActionContext actionContext) {

        String xid = actionContext.getXid();
        DeductStockDTO deductStock = ((JSONObject)actionContext.getActionContext("deductStock")).toJavaObject(DeductStockDTO.class);
        String skuCode = deductStock.getSkuCode();
        Integer saleQuantity = deductStock.getSaleQuantity();
        Integer originSaleStock = deductStock.getOriginSaleStock();
        Integer originSaledStock = deductStock.getOriginSaledStock();

        log.info("回滚：增加mysql销售库存，deductStock={}，xid={}",JSONObject.toJSONString(deductStock));

        // 空回滚处理
        if (TccResultHolder.isTrgNull(getClass(),skuCode,xid)){
        log.info("mysql：出现空回滚");
        insertEmptyRollbackTag(deductStock);
        return;
        }

        // 幂等 处理
        // try阶段 没有完成的情况下,不执行回滚，因为try阶段有本地事务，事务失败时已经进行了回滚
        //如果 try阶段成功，而且全局事务参与者失败，这里执行回滚

        if (!TccResultHolder.isTrySuccess(getClass(),skuCode,xid)){
            log.info("mysql:无需回滚");
            return;
        }

    // 1.还原 销售库存
    productStockDao.restoreSaleStock(skuCode,saleQuantity,originSaleStock-saleQuantity);
    // 2 删除库存扣减日志

    ProductStockLogDO productStockLogDO = productStockLogDao.getLog(deductStock.getOrderId(),deductStock.getSkuCode());

    if (ObjectUtils.isNotEmpty(productStockLogDO)){
        productStockLogDao.removeById(productStockLogDO.getId());
    }

    // 移除标识
        TccResultHolder.removeResult(getClass(),skuCode,xid);

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
