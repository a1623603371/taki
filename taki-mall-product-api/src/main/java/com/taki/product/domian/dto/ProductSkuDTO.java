package com.taki.product.domian.dto;

import com.taki.common.core.AbstractObject;

import java.io.Serializable;

/**
 * @ClassName ProductSkuDTO
 * @Description 商品SKU

 * @Author Long
 * @Date 2022/1/4 11:43
 * @Version 1.0
 */
public class ProductSkuDTO extends AbstractObject implements Serializable {


    private static final long serialVersionUID = 8904938402989925322L;

    /**
     * 商品Id
     */
    private String productId;

    /**
     * 商品类型
     */
    private Integer productType;

    /**
     *商品SKU 编码
     */
    private String skuCode;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 商品图片
     */
    private String productImage;

    /**
     *商品单位
     */
    private String productUnit;

    /**
     *商品销售价格
     */
    private Integer salePrice;


    /**
     * 采购价格
     */
    private Integer purchasePrice;

}
