package com.taki.inventory.service.impl;

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
    private LockMySqlStockTccService lockMySqlStockTccService;

    @Autowired
    private LockRedisStockTccService lockRedisStockTccService;
}
