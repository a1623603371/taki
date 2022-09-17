package com.taki.order.remote;

import com.taki.common.utli.ResponseData;
import com.taki.inventory.api.InventoryApi;
import com.taki.inventory.domain.request.DeductProductStockRequest;
import com.taki.market.domain.dto.CalculateOrderAmountDTO;
import com.taki.order.exception.OrderBizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @ClassName InventoryRemote
 * @Description 库存 远程 api
 * @Author Long
 * @Date 2022/6/8 15:56
 * @Version 1.0
 */
@Component
@Slf4j
public class InventoryRemote {


    @DubboReference(version = "1.0.0",retries = 0)
    private InventoryApi inventoryApi;


    public void deductProductStock(DeductProductStockRequest deductProductStockRequest){
        ResponseData<Boolean> responseData = inventoryApi.deductProductStock(deductProductStockRequest);

        if (responseData.getSuccess()){
            log.error("锁定商品仓库失败,订单号：{}", deductProductStockRequest.getOrderId());
            throw new OrderBizException(responseData.getCode(),responseData.getMessage());
        }
    }

}
