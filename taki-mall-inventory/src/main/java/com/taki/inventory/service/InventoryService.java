package com.taki.inventory.service;

import com.taki.inventory.domain.request.*;

import java.util.Map;

/**
 * @ClassName InventoryService
 * @Description 库存 service
 * @Author Long
 * @Date 2022/2/16 17:03
 * @Version 1.0
 */
public interface InventoryService {

//    /**
//     * @description: 锁定商品库存
//     * @param lockProductStockRequest 锁定商品库存请求参数
//     * @return 结果
//     * @author Long
//     * @date: 2022/2/17 10:16
//     */
//    Boolean lockProductStock(LockProductStockRequest lockProductStockRequest);



    /**
     * @description:  释放商品存储
     * @param releaseProductStockRequest 释放商品库存请求参数
     * @return 结果
     * @author Long
     * @date: 2022/2/17 10:24
     */
    Boolean releaseProductStock(ReleaseProductStockRequest releaseProductStockRequest);

    /**
     * @description:  扣减 库存
     * @param deductProductStockRequest 扣减商品库存请求
     * @return  处理结果
     * @author Long
     * @date: 2022/5/10 17:37
     */
    Boolean deductProductStock(DeductProductStockRequest deductProductStockRequest);

    /**
     * @description: 添加商品库存
     * @param addProductStockRequest 添加商品库存 请求
     * @return 处理结果
     * @author Long
     * @date: 2022/5/13 17:52
     */
    Boolean addProductStock(AddProductStockRequest addProductStockRequest);

    /**
     * @description: 修改商品库存
     * @param modifyProductStockRequest 修改 商品库存请求
     * @return  处理结果
     * @author Long
     * @date: 2022/5/13 18:33
     */
    Boolean modifyProductStock(ModifyProductStockRequest modifyProductStockRequest);

    /**
     * @description: 同步 数据库 与 缓存 的 库存
     * @param syncStockToCacheRequest 同步 数据库 与 缓存请求
     * @return
     * @author Long
     * @date: 2022/5/13 19:11
     */
    Boolean syncStockToCache(SyncStockToCacheRequest syncStockToCacheRequest);

    /**
     * @description:
     * @param skuCode
     * @return  java.lang.Object
     * @author Long
     * @date: 2022/6/20 14:31
     */
    Map<String,Object> getStockInfo(String skuCode);
}
