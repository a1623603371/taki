package com.taki.product.controller;

import com.taki.common.utli.ResponseData;
import com.taki.product.domain.entity.vo.ProductSkuVO;
import com.taki.product.domian.dto.ProductSkuDTO;
import com.taki.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName ProductController
 * @Description 商品 中心 controller 组件
 * @Author Long
 * @Date 2022/2/17 16:01
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/v1/product")
public class ProductController {

    @Autowired
    private ProductService productService;


    /**
     * @description: 根据商品SKU 编码 查询 商品SKU
     * @param skuCode 商品SKU 编码
     * @return 商品SKU
     * @author Long
     * @date: 2022/2/17 17:56
     */
    @GetMapping("/{skuCode}")
    public ResponseData<ProductSkuVO> getProductSkuByCode(@PathVariable("skuCode") String skuCode){

        ProductSkuDTO productSkuDTO = productService.getProductSkuByCode(skuCode);

        return ResponseData.success(productSkuDTO.clone(ProductSkuVO.class));
    }



}
