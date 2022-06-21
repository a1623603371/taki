package com.taki.inventory.controller;

import com.taki.common.utlis.ResponseData;
import com.taki.inventory.api.InventoryApi;
import com.taki.inventory.dao.ProductStockDao;
import com.taki.inventory.domain.request.*;
import com.taki.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @ClassName InventoryController
 * @Description 库存 controller 组件
 * @Author Long
 * @Date 2022/5/13 17:48
 * @Version 1.0
 */
@Slf4j
@RestController
@RequestMapping("/inventory")
public class InventoryController {


    @Autowired
    private InventoryService inventoryService;


    @Autowired
    private ProductStockDao productStockDao;

    /** 
     * @description:  新增商品库存
     * @param addProductStockRequest 新增商品库存请求
     * @return
     * @author Long
     * @date: 2022/5/13 17:50
     */ 
    @PostMapping("/addProductStock")
    public ResponseData<Boolean>  addProductStock (@RequestBody AddProductStockRequest addProductStockRequest){

        Boolean result =  inventoryService.addProductStock(addProductStockRequest);

        return ResponseData.success(result);
    }

    /**
     * @description: 修改商品库存
     * @param modifyProductStockRequest 修改 商品库存请求
     * @return  处理结果
     * @author Long
     * @date: 2022/5/13 18:33
     */
    @PostMapping("/modifyProductStock")
    public ResponseData<Boolean> modifyProductStock (@RequestBody ModifyProductStockRequest modifyProductStockRequest){

        Boolean result = inventoryService.modifyProductStock(modifyProductStockRequest);

        return ResponseData.success(result);
    }

    /**
     * @description: 同步 数据库 与 缓存 的 库存
     * @param syncStockToCacheRequest 同步 数据库 与 缓存请求
     * @return
     * @author Long
     * @date: 2022/5/13 19:11
     */
    @PostMapping("/syncStockToCache")
    public ResponseData<Boolean> syncStockToCache(@RequestBody SyncStockToCacheRequest syncStockToCacheRequest){

        Boolean result = inventoryService.syncStockToCache(syncStockToCacheRequest);


        return ResponseData.success(result);

    }


    /**
     * @description: 新增商品库存
     * @param
     * @return
     * @author Long
     * @date: 2022/6/20 14:27
     */
    @PostMapping("/deductProductStock")
    public  ResponseData<Boolean> deductProductStock(@RequestBody DeductProductStockRequest  request){


        Boolean result =  inventoryService.deductProductStock(request);

        return ResponseData.success(result);

    }

    
    /** 
     * @description: 获取商品库存
     * @param skuCode 商品编码
     * @return
     * @author Long
     * @date: 2022/6/20 14:30
     */ 
    @PostMapping("/getStockInfo")
    public  ResponseData<Map> getStockInfo(String skuCode){


        return ResponseData.success(inventoryService.getStockInfo(skuCode));

    }

    
    /** 
     * @description: 初始化测试数据
     * @param initMeasureDataRequest
     * @return  com.taki.common.utlis.ResponseData<java.lang.Boolean>
     * @author Long
     * @date: 2022/6/20 16:39
     */ 
    @PostMapping("/initMeasureData")
    public ResponseData<Boolean> initMeasureData(@RequestBody InitMeasureDataRequest initMeasureDataRequest){

        List<String > skuCodes = initMeasureDataRequest.getSkuCodes();

        if (!CollectionUtils.isEmpty(skuCodes)){

            //初始化 压测库存数据
            productStockDao.initMeasureInventoryData(skuCodes);


            skuCodes.forEach(skuCode->{
                SyncStockToCacheRequest syncStockToCacheRequest = new SyncStockToCacheRequest();

                syncStockToCacheRequest.setSkuCode(skuCode);

                //同步缓存
                inventoryService.syncStockToCache(syncStockToCacheRequest);
            });


        }

        return  ResponseData.success(true);
        
    }

}
