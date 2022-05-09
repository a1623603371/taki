package com.taki.order.manager.impl;

import com.taki.common.utlis.ObjectUtil;
import com.taki.common.utlis.ResponseData;
import com.taki.inventory.api.InventoryApi;
import com.taki.inventory.domain.request.LockProductStockRequest;
import com.taki.market.api.MarketApi;
import com.taki.market.domain.dto.CalculateOrderAmountDTO;
import com.taki.market.request.LockUserCouponRequest;
import com.taki.order.domain.request.CreateOrderRequest;
import com.taki.order.exception.OrderBizException;
import com.taki.order.manager.OrderManager;
import com.taki.product.domian.dto.ProductSkuDTO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName OrderMangerImpl
 * @Description TODO
 * @Author Long
 * @Date 2022/5/9 21:13
 * @Version 1.0
 */
@Service
@Slf4j
public class OrderMangerImpl implements OrderManager {


    /**
     * 库存服务
     */
    @DubboReference(version ="1.0.0" ,retries = 0)
    private InventoryApi inventoryApi;

    /**
     * 营销服务
     */
    @DubboReference(version = "1.0.0" ,retries = 0)
    private MarketApi marketApi;

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public Boolean createOrder(CreateOrderRequest createOrderRequest, List<ProductSkuDTO> productSkus, CalculateOrderAmountDTO calculateOrderAmount) {

        // 1锁定优惠券
        lockUserCoupon(createOrderRequest);

        // 2.锁定库存
        //7. 锁定商品库存
        lockProductStock(createOrderRequest);


        // 2 创建订单





        return null;
    }


    /**
     * @description: 锁定商品库存
     * @param createOrderRequest  生单请求
     * @return  void
     * @author Long
     * @date: 2022/1/6 10:12
     */
    private void lockProductStock(CreateOrderRequest createOrderRequest) {

        String orderId = createOrderRequest.getOrderId();

        List<LockProductStockRequest.OrderItemRequest> orderItemRequests = ObjectUtil.convertList(createOrderRequest.getOrderItemRequests(), LockProductStockRequest.OrderItemRequest.class);

        LockProductStockRequest lockProductStockRequest = createOrderRequest.clone(LockProductStockRequest.class);

        lockProductStockRequest.setOrderItemRequests(orderItemRequests);

        ResponseData<Boolean> responseResult = inventoryApi.lockProductStock(lockProductStockRequest);

        if (!responseResult.getSuccess()) {
            log.error("锁定商品仓库失败,订单号：{}", orderId);

            throw new OrderBizException(responseResult.getCode(), responseResult.getMessage());
        }
    }


    /**
     * @description: 锁定优惠券
     * @param createOrderRequest 生单请求
     * @return  void
     * @author Long
     * @date: 2022/1/6 9:42
     */
    private void lockUserCoupon(CreateOrderRequest createOrderRequest) {

        String coupon = createOrderRequest.getCouponId();

        if (StringUtils.isBlank(coupon)){
            return;
        }

        LockUserCouponRequest lockUserCouponRequest = createOrderRequest.clone(LockUserCouponRequest.class);

        ResponseData<Boolean> responseResult = marketApi.lockUserCoupon(lockUserCouponRequest);

        if (!responseResult.getSuccess()){
            log.error("锁定优惠券失败,订单号:{},优惠券Id:{},用户Id:{}",lockUserCouponRequest.getOrderId(),lockUserCouponRequest.getCouponId(),lockUserCouponRequest.getUserId());
            throw new OrderBizException(responseResult.getCode(),responseResult.getMessage());
        }

    }

}
