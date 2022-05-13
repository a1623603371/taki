package com.taki.inventory.api;

import com.google.common.collect.Lists;
import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.redis.RedisLock;
import com.taki.common.utlis.ResponseData;
import com.taki.inventory.domain.request.CancelOrderReleaseProductStockRequest;
import com.taki.inventory.domain.request.DeductProductStockRequest;
import com.taki.inventory.domain.request.ReleaseProductStockRequest;
import com.taki.inventory.exception.InventoryBizException;
import com.taki.inventory.exception.InventoryErrorCodeEnum;
import com.taki.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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


    @Autowired
    private RedisLock redisLock;

//    @Override
//    public ResponseData<Boolean> lockProductStock(LockProductStockRequest lockProductStockRequest) {
//
//        try {
//            Boolean result = inventoryService.lockProductStock(lockProductStockRequest);
//            return ResponseData.success(result);
//        }catch (InventoryBizException e){
//            log.error("biz error",e);
//            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());
//        }catch (Exception e){
//            log.error("system error",e);
//            return ResponseData.error(e.getMessage());
//        }
//    }

    @Override
    public ResponseData<Boolean> cancelOrderReleaseProductStock(ReleaseProductStockRequest releaseProductStockRequest) {

        log.info("回滚 库存 ,orderId:{}",releaseProductStockRequest.getOrderId());

        List<String> redisKeyList = Lists.newArrayList();

        releaseProductStockRequest.getOrderItemRequests().forEach(orderItemRequest -> {

            String lockKey = RedisLockKeyConstants.RELEASE_PRODUCT_STOCK_KEY + orderItemRequest.getSkuCode();
            redisKeyList.add(lockKey);
        });

            Boolean lock = redisLock.multiLock(redisKeyList);

            if (!lock) {
                throw new InventoryBizException(InventoryErrorCodeEnum.RELEASE_PRODUCT_SKU_STOCK_ERROR);
            }
        try {

            Boolean result = inventoryService.releaseProductStock(releaseProductStockRequest);
            return ResponseData.success(result);
        }catch (InventoryBizException e){
            log.error("biz error",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());
        }catch (Exception e){
            log.error("system error",e);
            return ResponseData.error(e.getMessage());
        }finally {
            redisLock.unMultiLock(redisKeyList);
        }
    }

    @Override
    public ResponseData<Boolean> deductProductStock(DeductProductStockRequest deductProductStockRequest) {
        try {
            Boolean result = inventoryService.deductProductStock(deductProductStockRequest);
            return ResponseData.success(result);
        }catch (InventoryBizException e){
            log.error("biz error",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());
        }catch (Exception e){
            log.error("system error",e);
            return ResponseData.error(e.getMessage());
        }
    }
}
