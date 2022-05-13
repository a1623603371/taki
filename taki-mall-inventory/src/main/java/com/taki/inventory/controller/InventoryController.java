package com.taki.inventory.controller;

import com.taki.common.utlis.ResponseData;
import com.taki.inventory.api.InventoryApi;
import com.taki.inventory.domain.request.AddProductStockRequest;
import com.taki.inventory.domain.request.ModifyProductStockRequest;
import com.taki.inventory.domain.request.SyncStockToCacheRequest;
import com.taki.inventory.service.InventoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
