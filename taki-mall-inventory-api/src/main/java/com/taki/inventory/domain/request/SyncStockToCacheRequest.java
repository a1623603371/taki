package com.taki.inventory.domain.request;

import lombok.Data;

/**
 * @ClassName SyncStockToCacheRequest
 * @Description 同步商品 sku 库存数据到缓存
 * @Author Long
 * @Date 2022/5/13 20:45
 * @Version 1.0
 */
@Data
public class SyncStockToCacheRequest {

    /**
     * 商品SKU编码
     */
    private String skuCode;
}
