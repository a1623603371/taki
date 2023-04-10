package com.taki.inventory.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.constants.CoreConstants;
import com.taki.common.redis.RedisCache;
import com.taki.common.redis.RedisLock;
import com.taki.common.utli.LoggerFormat;
import com.taki.common.utli.ParamCheckUtil;
import com.taki.inventory.cache.CacheSupport;
import com.taki.inventory.dao.ProductStockDao;
import com.taki.inventory.dao.ProductStockLogDao;
import com.taki.inventory.domain.dto.DeductStockDTO;
import com.taki.inventory.domain.entity.ProductStockDO;
import com.taki.inventory.domain.entity.ProductStockLogDO;
import com.taki.inventory.domain.request.*;
import com.taki.inventory.enums.StockLogStatusEnum;
import com.taki.inventory.exception.InventoryBizException;
import com.taki.inventory.exception.InventoryErrorCodeEnum;
import com.taki.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
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


    @Autowired
    private ModifyProductStockProcessor modifyProductStockProcessor;


    @Autowired
    private  SyncStockToCacheProcessor syncStockToCacheProcessor;

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

        String orderId = releaseProductStockRequest.getOrderId();
        for (ReleaseProductStockRequest.OrderItemRequest orderItemRequest : orderItemRequests) {
            String skuCode = orderItemRequest.getSkuCode();
            String lockKey = RedisLockKeyConstants.RELEASE_PRODUCT_STOCK_KEY + skuCode;
            //1、添加redis释放库存锁，作用
            // (1)防同一笔订单重复释放
            // (2)重量级锁，保证mysql+redis释放库存的原子性，同一时间只能有一个订单来释放，
            //    需要锁查询+扣库存

            Boolean locked = redisLock.tryLock(lockKey,CoreConstants.DEFAULT_WAIT_SECONDS);

            if (!locked){
                log.error(LoggerFormat.build()
                        .remark("无法获取释放库存锁")
                        .data("orderId",orderId)
                        .data("skuCode",skuCode)
                        .finish());

                throw new InventoryBizException(InventoryErrorCodeEnum.RELEASE_PRODUCT_SKU_STOCK_ERROR);
            }
            try {
                //2 查询mysql库存数据
                ProductStockDO productStockDO = productStockDao.getBySkuCode(skuCode);

                if (ObjectUtils.isEmpty(productStockDO)){
                    throw new InventoryBizException(InventoryErrorCodeEnum.PRODUCT_SKU_STOCK_NOT_FOUND_ERROR);
                }

                // 3.查询redis 库存数据
                String productStockKey = CacheSupport.buildProductStockKey(skuCode);

                Map<String,String>  productStockValue = redisCache.hGetAll(productStockKey);

                if (productStockValue.isEmpty()){
                    //如果 查询不到redis 库存数据，将mysql 库存数据 放入 redis，已mysql数据为准
                    addProductStockProcessor.addStockToRedis(productStockDO);
                }

                Integer saleQuantity = orderItemRequest.getSaleQuantity();

                //4.效验是否释放库存
                ProductStockLogDO productStockLog = productStockLogDao.getLog(orderId,skuCode);

                if (!ObjectUtils.isEmpty(productStockLog) && StockLogStatusEnum.RELEASE.getCode().equals(productStockLog.getStatus())){
                    log.info(LoggerFormat.build()
                            .remark("已释放库存")
                            .data("orderId",orderId)
                            .data("skuCode",skuCode)
                            .finish());
                    return true;
                }
            }finally {
                redisLock.unLock(lockKey);
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean deductProductStock(DeductProductStockRequest deductProductStockRequest) {


        log.info(LoggerFormat
                .build()
                .remark("deductProductStock->request")
                .data("request",deductProductStockRequest)
                .finish());

        // 检查入参
        checkLockProductStockRequest(deductProductStockRequest);
        String orderId = deductProductStockRequest.getOrderId();
        List<DeductProductStockRequest.OrderItemRequest> orderItemRequests = deductProductStockRequest.getOrderItemRequests();

        for (DeductProductStockRequest.OrderItemRequest orderItemRequest : orderItemRequests) {


            String skuCode = orderItemRequest.getSkuCode();

            String lockKey = RedisLockKeyConstants.DEDUCT_PRODUCT_STOCK_KEY + skuCode;
            try {
                // 1.添加redis锁扣减库存锁
                // （1）防止一笔订单重复扣减
                //重量级锁，保证mysql + redis 扣 库存的原子性，同一时间只能有一个订单扣减
                //需要锁查询+扣库存
                // 获取不到锁，阻塞等待,默认等待3s
                Boolean lockResult = redisLock.tryLock(lockKey, CoreConstants.DEFAULT_WAIT_SECONDS);

                if(!lockResult){
                    log.error(LoggerFormat.build()
                            .remark("无法获取扣减库存锁")
                            .data("orderId",orderId)
                            .data("skuCode",skuCode)
                            .finish());
                    throw new InventoryBizException(InventoryErrorCodeEnum.DEDUCT_PRODUCT_SKU_STOCK_ERROR);
                }


            // 2.查询 数据库 库存数据
            ProductStockDO productStockDO = productStockDao.getBySkuCode(skuCode);
                log.info(LoggerFormat.build()
                        .remark("查询mysql库存数据")
                        .data("orderId", orderId)
                        .data("productStockDO", productStockDO)
                        .finish());
            if (productStockDO == null){
                log.error(LoggerFormat.build()
                        .remark("商品库存记录不存在")
                        .data("skuCode", skuCode)
                        .finish());
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

            //4.查询库存扣减日志
            ProductStockLogDO productStockLogDO = productStockLogDao.getLog(orderId,skuCode);

            if (!ObjectUtils.isEmpty(productStockLogDO)){
                log.info(LoggerFormat.build()
                        .remark("已扣减，扣减库存日志已存在")
                        .data("orderId", orderId)
                        .data("skuCode", skuCode)
                        .finish());
                return true;
            }
                Integer saleQuantity = orderItemRequest.getSaleQuantity();

                // 5.执行库存扣减
                DeductStockDTO deductStockDTO = new DeductStockDTO(orderId,skuCode,Long.valueOf(saleQuantity),productStockDO);

                deductProductProcessor.deduct(deductStockDTO);

            }finally {
                redisLock.unLock(lockKey);
            }
        }
        log.info(LoggerFormat.build()
                .remark("deductProductStock->response")
                .finish());
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean addProductStock(AddProductStockRequest addProductStockRequest) {
        log.info("新增商品库存: request:{}", JSONObject.toJSONString(addProductStockRequest));

        // 1. 效验参数
        checkAddProductStockRequest(addProductStockRequest);

        // 2.查询 商品库存
        ProductStockDO productStockDO = productStockDao.getBySkuCode(addProductStockRequest.getSkuCode());

        ParamCheckUtil.checkObjectNonNull(productStockDO,InventoryErrorCodeEnum.PRODUCT_SKU_STOCK_NOT_FOUND_ERROR);

        // 3.添加 redis 锁 防止并发
        String lockKey = RedisLockKeyConstants.ADD_PRODUCT_STOCK_KEY +addProductStockRequest.getSkuCode();

         Boolean result = redisLock.tryLock(lockKey);

         if (!result){
             throw new InventoryBizException(InventoryErrorCodeEnum.ADD_PRODUCT_SKU_STOCK_ERROR);
         }

         try {
            // 执行 添加库存逻辑
            addProductStockProcessor.doAdd(addProductStockRequest);
         }finally {
             redisLock.unLock(lockKey);
         }


        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean modifyProductStock(ModifyProductStockRequest modifyProductStockRequest) {

        log.info("调证商品库存:request :{}",JSONObject.toJSONString(modifyProductStockRequest));

        String skuCode = modifyProductStockRequest.getSkuCode();
        //1.效验入参
        checkModifyProductStockRequest(modifyProductStockRequest);

        // 查询 商品库存
        ProductStockDO productStockDO = productStockDao.getBySkuCode(skuCode);

        if (ObjectUtils.isEmpty(productStockDO)){
            throw new InventoryBizException(InventoryErrorCodeEnum.PRODUCT_SKU_STOCK_NOT_FOUND_ERROR);
        }

        //效验库存被扣减后是否小于 0
        Long saleIncremental = modifyProductStockRequest.getStockIncremental();

        Long saleStockQuantity = productStockDO.getSaleStockQuantity();

        if (saleStockQuantity - saleIncremental < 0){

            throw new InventoryBizException(InventoryErrorCodeEnum.SALE_STOCK_QUANTITY_CANNOT_BE_NEGATIVE_NUMBER);

        }

        // 加分布式锁，保证 mysql 和redis 数据一致性

        String lockKey = RedisLockKeyConstants.MODIFY_PRODUCT_STOCK_KEY + modifyProductStockRequest.getSkuCode();

        Boolean lock = redisLock.tryLock(lockKey);

        if (!lock){
            throw new InventoryBizException(InventoryErrorCodeEnum.MODIFY_PRODUCT_SKU_STOCK_ERROR);
        }

        try {
            modifyProductStockProcessor.doModify(productStockDO,saleIncremental);
        }finally {
            redisLock.unLock(lockKey);
        }

        return true;
    }

    @Override
    public Boolean syncStockToCache(SyncStockToCacheRequest request) {

        String skuCode = request.getSkuCode();

        //1.效验参数
        ParamCheckUtil.checkStringNonEmpty(skuCode,InventoryErrorCodeEnum.SKU_CODE_IS_EMPTY);

        syncStockToCacheProcessor.doSync(skuCode);

        return true;
    }

    @Override
    public Map<String, Object> getStockInfo(String skuCode) {

        ProductStockDO productStock = productStockDao.getBySkuCode(skuCode);

        if (ObjectUtils.isEmpty(productStock)){

            return null;
        }

        String productStockKey = CacheSupport.buildProductStockKey(skuCode);

        Map<String,String> productStockVale = redisCache.hGetAll(productStockKey);

        Map<String,Object> result = new HashMap<>();

        result.put("mysql",productStock);
        result.put("redis",productStockVale);
        return result;
    }

    /**
     * @description: 检查 修改 商品库存请求
     * @param modifyProductStockRequest 修改 商品库存请求 数据
     * @return  void
     * @author Long
     * @date: 2022/5/13 18:39
     */
    private void checkModifyProductStockRequest(ModifyProductStockRequest modifyProductStockRequest) {
        ParamCheckUtil.checkStringNonEmpty(modifyProductStockRequest.getSkuCode(),InventoryErrorCodeEnum.SKU_CODE_IS_EMPTY);
        ParamCheckUtil.checkObjectNonNull(modifyProductStockRequest.getStockIncremental(),InventoryErrorCodeEnum.SALE_STOCK_INCREMENTAL_QUANTITY_IS_EMPTY);

        ParamCheckUtil.checkLongMin(modifyProductStockRequest.getStockIncremental(), 0L,InventoryErrorCodeEnum.SALE_STOCK_INCREMENTAL_QUANTITY_CANNOT_BE_ZERO);


    }

    /**
     * @description: 检查 添加商品库存参数 请求
     * @param addProductStockRequest 添加商品库存请求
     * @return  void
     * @author Long
     * @date: 2022/5/13 17:54
     */
    private void checkAddProductStockRequest(AddProductStockRequest addProductStockRequest) {
        ParamCheckUtil.checkStringNonEmpty(addProductStockRequest.getSkuCode(),InventoryErrorCodeEnum.SKU_CODE_IS_EMPTY);

        ParamCheckUtil.checkObjectNonNull(addProductStockRequest.getSaleStockQuantity(),InventoryErrorCodeEnum.SALE_STOCK_QUANTITY_IS_EMPTY);

        ParamCheckUtil.checkLongMin(addProductStockRequest.getSaleStockQuantity(),0L,InventoryErrorCodeEnum.SALE_STOCK_INCREMENTAL_QUANTITY_CANNOT_BE_ZERO);


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
