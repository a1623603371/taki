package com.taki.inventory.service.impl;

import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.redis.RedisCache;
import com.taki.common.redis.RedisLock;
import com.taki.common.utlis.ParamCheckUtil;
import com.taki.inventory.cache.CacheSupport;
import com.taki.inventory.dao.ProductStockDao;
import com.taki.inventory.dao.ProductStockLogDao;
import com.taki.inventory.domain.dto.DeductStockDTO;
import com.taki.inventory.domain.entity.ProductStockDO;
import com.taki.inventory.domain.entity.ProductStockLogDO;
import com.taki.inventory.domain.request.DeductProductStockRequest;
import com.taki.inventory.domain.request.ReleaseProductStockRequest;
import com.taki.inventory.exception.InventoryBizException;
import com.taki.inventory.exception.InventoryErrorCodeEnum;
import com.taki.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.config.CacheManagementConfigUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.w3c.dom.stylesheets.LinkStyle;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

/**
 * @ClassName InventoryServiceImpl
 * @Description 库存 service 服务
 * @Author Long
 * @Date 2022/2/16 17:04
 * @Version 1.0
 */
@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService {


    @Autowired
    private ProductStockDao productStockDao;

    @Autowired
    private ProductStockLogDao productStockLogDao;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private AddProductStockProcessor addProductStockProcessor;


    @Autowired
    private DeductProductStockProcessor deductProductProcessor;

//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public Boolean lockProductStock(LockProductStockRequest lockProductStockRequest) {
//
//        // 检查入参
//        checkLockProductStockRequest(lockProductStockRequest);
//
//        List<LockProductStockRequest.OrderItemRequest> orderItemRequests = lockProductStockRequest.getOrderItemRequests();
//
//        orderItemRequests.forEach(orderItemRequest -> {
//            String skuCode = orderItemRequest.getSkuCode();
//            ProductStockDO productStockDO = productStockDao.getBySkuCode(skuCode);
//            if (productStockDO == null){
//                throw new InventoryBizException(InventoryErrorCodeEnum.PRODUCT_SKU_STOCK_ERROR);
//            }
//            Integer saleQuantity = orderItemRequest.getSaleQuantity();
//
//            Boolean result = productStockDao.lockProductStock(skuCode,saleQuantity);
//
//            if (!result){
//                throw new InventoryBizException(InventoryErrorCodeEnum.LOCK_PRODUCT_SKU_STOCK_ERROR);
//            }
//
//        });
//        return true;
//    }

    /**
     * @description: 检查商品
     * @param lockProductStockRequest 锁定商品库存请求
     * @return  void
     * @author Long
     * @date: 2022/2/17 10:43
     */
    private void checkLockProductStockRequest(DeductProductStockRequest lockProductStockRequest) {
        String orderId = lockProductStockRequest.getOrderId();
        ParamCheckUtil.checkStringNonEmpty(orderId);
        List<DeductProductStockRequest.OrderItemRequest> orderItemRequests = lockProductStockRequest.getOrderItemRequests();
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
                throw new InventoryBizException(InventoryErrorCodeEnum.PRODUCT_SKU_STOCK_NOT_FOUND_ERROR);
            }

            Integer saleQuantity = orderItemRequest.getSaleQuantity();

            Boolean result = productStockDao.releaseProductStock(skuCode,saleQuantity);

            if (!result){
                throw new InventoryBizException(InventoryErrorCodeEnum.RELEASE_PRODUCT_SKU_STOCK_ERROR);
            }

        });
        return true;
    }

    @Override
    public Boolean deductProductStock(DeductProductStockRequest deductProductStockRequest) {

        // 检查入参
        checkLockProductStockRequest(deductProductStockRequest);
        String orderId = deductProductStockRequest.getOrderId();
        List<DeductProductStockRequest.OrderItemRequest> orderItemRequests = deductProductStockRequest.getOrderItemRequests();

        orderItemRequests.forEach(orderItemRequest -> {
            String skuCode = orderItemRequest.getSkuCode();
            // 1.查询 数据库 库存数据
            ProductStockDO productStockDO = productStockDao.getBySkuCode(skuCode);
            if (productStockDO == null){
                log.error("商品库存记录不存在，skuCode={}",skuCode);
                throw new InventoryBizException(InventoryErrorCodeEnum.PRODUCT_SKU_STOCK_NOT_FOUND_ERROR);
            }

            //2 查询 redis 缓存数据
            String productStockKey = CacheSupport.buildProductStockKey(skuCode);

            Map<String,String> productStockValue = redisCache.hGetAll(productStockKey);

            if (productStockValue.isEmpty()){
                //如果缓存中的数据为空 就将 mysql 数据库 的 数据设置 放入缓存中 已 mysql 数据为准
                addProductStockProcessor.addStockToRedis(productStockDO);
            }

            //3.添加redis 锁 ，防止 bingfa

            String lockKey = MessageFormat.format(RedisLockKeyConstants.RELEASE_PRODUCT_STOCK_KEY,orderId,skuCode);

            Boolean result = redisLock.lock(lockKey);

            if (!result){
                log.error("无法获取扣减库存锁，orderId = {},skuCode={}",orderId,skuCode);
                throw new InventoryBizException(InventoryErrorCodeEnum.DEDUCT_PRODUCT_SKU_STOCK_ERROR);
            }


            try {

            //4.查询库存扣减日志
            ProductStockLogDO productStockLogDO = productStockLogDao.getLog(orderId,skuCode);

            if (!ObjectUtils.isEmpty(productStockDO)){
                log.error("已扣减，扣减库存日志已存在，orderId={}，skuCode={}",orderId,skuCode);
            }
                Integer saleQuantity = orderItemRequest.getSaleQuantity();
                Integer originSaleStock =  productStockDO.getSaleStockQuantity().intValue();
                Integer originSaledStock = productStockDO.getSaledStockQuantity().intValue();

                // 5.执行库存扣减
                DeductStockDTO deductStockDTO = new DeductStockDTO(orderId,skuCode,saleQuantity,originSaleStock,originSaledStock);

                deductProductProcessor.deduct(deductStockDTO);

            }finally {
                redisLock.unLock(lockKey);
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
