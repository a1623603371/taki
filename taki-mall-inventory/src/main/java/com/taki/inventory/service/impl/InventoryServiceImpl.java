package com.taki.inventory.service.impl;

import com.taki.common.utlis.ParamCheckUtil;
import com.taki.inventory.dao.ProductStockDao;
import com.taki.inventory.domain.entity.ProductStockDO;
import com.taki.inventory.domain.request.LockProductStockRequest;
import com.taki.inventory.domain.request.ReleaseProductStockRequest;
import com.taki.inventory.exception.InventoryBizException;
import com.taki.inventory.exception.InventoryErrorCodeEnum;
import com.taki.inventory.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * @ClassName InventoryServiceImpl
 * @Description 库存 service 服务
 * @Author Long
 * @Date 2022/2/16 17:04
 * @Version 1.0
 */
@Service
public class InventoryServiceImpl implements InventoryService {


    @Autowired
    private ProductStockDao productStockDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean lockProductStock(LockProductStockRequest lockProductStockRequest) {

        // 检查入参
        checkLockProductStockRequest(lockProductStockRequest);

        List<LockProductStockRequest.OrderItemRequest> orderItemRequests = lockProductStockRequest.getOrderItemRequests();

        orderItemRequests.forEach(orderItemRequest -> {
            String skuCode = orderItemRequest.getSkuCode();
            ProductStockDO productStockDO = productStockDao.getBySkuCode(skuCode);
            if (productStockDO == null){
                throw new InventoryBizException(InventoryErrorCodeEnum.PRODUCT_SKU_STOCK_ERROR);
            }
            Integer saleQuantity = orderItemRequest.getSaleQuantity();

            Boolean result = productStockDao.lockProductStock(skuCode,saleQuantity);

            if (!result){
                throw new InventoryBizException(InventoryErrorCodeEnum.LOCK_PRODUCT_SKU_STOCK_ERROR);
            }

        });
        return true;
    }

    /**
     * @description: 检查商品
     * @param lockProductStockRequest 锁定商品库存请求
     * @return  void
     * @author Long
     * @date: 2022/2/17 10:43
     */
    private void checkLockProductStockRequest(LockProductStockRequest lockProductStockRequest) {
        String orderId = lockProductStockRequest.getOrderId();
        ParamCheckUtil.checkStringNonEmpty(orderId);
        List<LockProductStockRequest.OrderItemRequest> orderItemRequests = lockProductStockRequest.getOrderItemRequests();
        ParamCheckUtil.checkCollectionNonEmpty(orderItemRequests);

    }


    @Transactional(rollbackFor = Exception.class)
    @Override
    public Boolean releaseProductStock(ReleaseProductStockRequest releaseProductStockRequest) {
        checkReleaseProductStockRequest(releaseProductStockRequest);

        List<ReleaseProductStockRequest.OrderItemRequest> orderItemRequests = releaseProductStockRequest.getOrderItemRequests();

        orderItemRequests.forEach(orderItemRequest -> {

            String skuCode = orderItemRequest.getSkuCode();

            ProductStockDO productStockDO = productStockDao.getBySkuCode(skuCode);

            if (ObjectUtils.isEmpty(productStockDO)){
                throw new InventoryBizException(InventoryErrorCodeEnum.PRODUCT_SKU_STOCK_ERROR);
            }

            Integer saleQuantity = orderItemRequest.getSaleQuantity();

            Boolean result = productStockDao.releaseProductStock(skuCode,saleQuantity);

            if (!result){
                throw new InventoryBizException(InventoryErrorCodeEnum.RELEASE_PRODUCT_SKU_STOCK_ERROR);
            }

        });
        return true;
    }

    /**
     * @description: 检查释放商品库存 请求参数
     * @param releaseProductStockRequest 释放商品库存 请求参数
     * @return  void
     * @author Long
     * @date: 2022/2/17 10:58
     */
    private void checkReleaseProductStockRequest(ReleaseProductStockRequest releaseProductStockRequest) {
        String orderId = releaseProductStockRequest.getOrderId();
        ParamCheckUtil.checkStringNonEmpty(orderId);
        List<ReleaseProductStockRequest.OrderItemRequest> orderItemRequests = releaseProductStockRequest.getOrderItemRequests();
        ParamCheckUtil.checkCollectionNonEmpty(orderItemRequests);
    }
}
