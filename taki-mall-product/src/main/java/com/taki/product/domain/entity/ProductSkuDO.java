package com.taki.product.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName ProductSkuDO
 * @Description 商品SKU
 * @Author Long
 * @Date 2022/2/17 16:09
 * @Version 1.0
 */
@Data
@TableName("product_sku")
public class ProductSkuDO extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -550000449593044214L;


    /**
     * 商品Id
     */
    private String productId;

    /**
     * 商品类型
     */
    private Integer productType;

    /**
     * 商品Sku编码
     */
    private String skuCode;


    /**
     * 商品名称
     */
    private String  productName;

    /**
     * 商品图片
     */
    private String productImg;

    /**
     * 商品单位
     */
    private String productUnit;

    /**
     * 商品销售价格
     */
    private BigDecimal salePrice;


    /**
     * 商品采购价格
     */
    @TableField("")
    private BigDecimal purchasePrice;



    public static final  String SKU_CODE = "sku_code";
}
