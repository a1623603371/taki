package com.taki.inventory.api;

import com.taki.common.utlis.ResponseData;
import com.taki.inventory.domain.request.DeductProductStockRequest;
import com.taki.inventory.domain.request.ReleaseProductStockRequest;
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

    @Deprecated
   // ResponseData<Boolean> lockProductStock(LockProductStockRequest lockProductStockRequest);
 */


    /**
     * @description:  取消订单 释放库存
     * @param releaseProductStockRequest 取消订单释放商品库存 请求参数
     * @return 处理结果
     * @author Long
     * @date: 2022/2/17 10:28
     */
    ResponseData<Boolean> cancelOrderReleaseProductStock(ReleaseProductStockRequest releaseProductStockRequest);

    /** 
     * @description: 扣减库存
     * @param deductProductStockRequest 扣减商品库存 请求
     * @return  处理结果
     * @author Long
     * @date: 2022/5/10 15:03
     */ 
    ResponseData<Boolean> deductProductStock(DeductProductStockRequest deductProductStockRequest);
}
