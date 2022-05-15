package com.taki.fulfill.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName OrderFulfillItemDO
 * @Description 订单履约条目表
 * @Author Long
 * @Date 2022/5/15 18:46
 * @Version 1.0
 */
@Data
@TableName("order_fulfill_item")
public class OrderFulfillItemDO extends BaseEntity implements Serializable {


    private static final long serialVersionUID = -437326019730509568L;

    /**
     * 履约单Id
     */
    private String fulfillId;

    /**
     * 商品Id
     */
    private String skuCode;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 销售单价
     */
    private BigDecimal salePrice;


    /**
     * 销售数量
     */
    private Integer saleQuantity;

    /**
     * 商品单位
     */
    private String productUnit;

    /**
     * 支付价格
     */
    private BigDecimal payAmount;

    /**
     * 当前商品支付原价
     */
    private Integer originAmount;


    public static final  String  FULFILL_ID = "fulfill_id";
}
