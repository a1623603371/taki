package com.taki.inventory.api;

import com.taki.common.utlis.ResponseData;
import com.taki.inventory.domain.request.LockProductStockRequest;

/**
 * @ClassName InventoryApi
 * @Description 库存API
 * @Author Long
 * @Date 2022/1/6 10:17
 * @Version 1.0
 */
public interface InventoryApi {


    /***
     * @description: 锁定商品库存
     * @param lockProductStockRequest 锁定商品库存请求
     * @return  执行结果数据
     * @author Long
     * @date: 2022/1/6 10:18
     */
    ResponseData<Boolean> lockProductStock(LockProductStockRequest lockProductStockRequest);
}
