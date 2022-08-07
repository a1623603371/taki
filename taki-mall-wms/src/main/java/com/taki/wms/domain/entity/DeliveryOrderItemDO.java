package com.taki.wms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName DeliveryOrderItemDO
 * @Description 出库单条目
 * @Author Long
 * @Date 2022/5/16 20:34
 * @Version 1.0
 */
@Data
@TableName("delivery_order_item")
public class DeliveryOrderItemDO extends BaseEntity implements Serializable {

    /**
     * 出库单Id
     */
    private String deliveryOrderId;

    /**
     * 商品sku 编码
     */
    private String skuCode;

    /**
     * 商品名称
     */
    private String  productName;


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
     * 当前商品支付原总价
     */
    private BigDecimal originAmount;

    /**
     * 付款金额
     */
    private BigDecimal payAmount;


    /**
     * 拣货数量
     */
    private Integer pickingCount;

    /**
     * 拣货仓库柜货Id
     */
    private Integer skuContainerId;


    public  static  final  String DELIVERY_ORDER_ID = "delivery_order_id";

}
