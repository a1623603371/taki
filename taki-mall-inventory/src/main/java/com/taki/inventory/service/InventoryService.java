package com.taki.inventory.service;

import com.taki.inventory.domain.request.LockProductStockRequest;
import com.taki.inventory.domain.request.ReleaseProductStockRequest;

/**
 * @ClassName InventoryService
 * @Description 库存 service
 * @Author Long
 * @Date 2022/2/16 17:03
 * @Version 1.0
 */
public interface InventoryService {

    /**
     * @description: 锁定商品库存
     * @param lockProductStockRequest 锁定商品库存请求参数
     * @return 结果
     * @author Long
     * @date: 2022/2/17 10:16
     */
    Boolean lockProductStock(LockProductStockRequest lockProductStockRequest);



    /**
     * @description:  释放商品存储
     * @param releaseProductStockRequest 释放商品库存请求参数
     * @return 结果
     * @author Long
     * @date: 2022/2/17 10:24
     */
    Boolean releaseProductStock(ReleaseProductStockRequest releaseProductStockRequest);
}
