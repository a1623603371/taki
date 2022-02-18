package com.taki.product.service;

import com.taki.product.domian.dto.ProductSkuDTO;

/**
 * @ClassName ProductService
 * @Description 商品 service 接口
 * @Author Long
 * @Date 2022/2/17 16:03
 * @Version 1.0
 */
public interface ProductService {


    /**
     * @description: 根据商品sku 编码查询商品sku
     * @param skuCode sku 编码
     * @return  商品SKU 信息
     * @author Long
     * @date: 2022/2/17 17:36
     */
    ProductSkuDTO getProductSkuByCode(String skuCode);
}
