package com.taki.inventory.service.impl;

import com.taki.inventory.domain.dto.DeductStockDTO;
import com.taki.inventory.exception.InventoryBizException;
import com.taki.inventory.exception.InventoryErrorCodeEnum;
import com.taki.inventory.tcc.LockMysqlStockTccService;
import com.taki.inventory.tcc.LockRedisStockTccService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName DeductProductStockProcessor
 * @Description 扣减商品库存处理器
 * @Author Long
 * @Date 2022/5/11 21:42
 * @Version 1.0
 */
@Component
@Slf4j
public class DeductProductStockProcessor {

    @Autowired
    private LockMysqlStockTccService lockMySqlStockTccService;

    @Autowired
    private LockRedisStockTccService lockRedisStockTccService;


    /**
     * @description: 执行扣减库存逻辑
     * @param deductStock 扣减库存
     * @return  void
     * @author Long
     * @date: 2022/5/12 18:04
     */
    public void deduct(DeductStockDTO deductStock){

        //1.执行 mysql库存扣减
     Boolean mysqlResult =    lockMySqlStockTccService.deductStock(null,deductStock);

     if (!mysqlResult){
         throw new InventoryBizException(InventoryErrorCodeEnum.PRODUCT_SKU_STOCK_NOT_FOUND_ERROR);
     }

        //2 执行 redis 库存扣减
       Boolean redisResult =  lockRedisStockTccService.deductStock(null,deductStock);

        if (!redisResult){
            throw new InventoryBizException(InventoryErrorCodeEnum.PRODUCT_SKU_STOCK_NOT_FOUND_ERROR);
        }
    }
}
