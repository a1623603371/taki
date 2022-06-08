package com.taki.product.service;

import com.taki.product.domian.dto.ProductSkuDTO;

import java.util.List;

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

    /**
     * @description: 根据 商品编码集合 批量查询商品
     * @param skuCodes 商品编码集合
     * @return
     * @author Long
     * @date: 2022/6/8 15:35
     */
    List<ProductSkuDTO> listProductSkuByCode(List<String> skuCodes);
}
