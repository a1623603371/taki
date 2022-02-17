package com.taki.inventory.api;

import com.taki.common.utlis.ResponseData;
import com.taki.inventory.domain.request.CancelOrderReleaseProductStockRequest;
import com.taki.inventory.domain.request.LockProductStockRequest;
import com.taki.inventory.domain.request.ReleaseProductStockRequest;
import com.taki.inventory.exception.InventoryBizException;
import com.taki.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName InventoryApi
 * @Description TODO
 * @Author Long
 * @Date 2022/2/17 11:47
 * @Version 1.0
 */
@DubboService(version = "1.0.0" ,interfaceClass = InventoryApi.class,retries = 0)
@Slf4j
public class InventoryApiImpl implements InventoryApi {


    @Autowired
    private InventoryService inventoryService;


    @Override
    public ResponseData<Boolean> lockProductStock(LockProductStockRequest lockProductStockRequest) {

        try {
            Boolean result = inventoryService.lockProductStock(lockProductStockRequest);
            return ResponseData.success(result);
        }catch (InventoryBizException e){
            log.error("biz error",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());
        }catch (Exception e){
            log.error("system error",e);
            return ResponseData.error(e.getMessage());
        }
    }

    @Override
    public ResponseData<Boolean> cancelOrderReleaseProductStock(CancelOrderReleaseProductStockRequest cancelOrderReleaseProductStockRequest) {
        try {
        log.info("回滚 库存 ,orderId:{}",cancelOrderReleaseProductStockRequest.getOrderId());
         //   Boolean result = inventoryService.releaseProductStock(releaseProductStockRequest);
            return ResponseData.success(true);
        }catch (InventoryBizException e){
            log.error("biz error",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());
        }catch (Exception e){
            log.error("system error",e);
            return ResponseData.error(e.getMessage());
        }
    }
}
