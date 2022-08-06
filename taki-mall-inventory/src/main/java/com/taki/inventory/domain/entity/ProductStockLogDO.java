package com.taki.inventory.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ProductStockLogDO
 * @Description 商品库存日志
 * @Author Long
 * @Date 2022/5/11 18:32
 * @Version 1.0
 */
@Data
@TableName("product_stock_log")
public class ProductStockLogDO extends BaseEntity implements Serializable {


    private static final long serialVersionUID = -257550543803894322L;


    /**
     * 订单Id
     */
    private String orderId;


    /**
     * 商品sku编码
     */
    private String skuCode;

    /**
     *原始销售库存
     */
    private Long originSaleStockQuantity;

    /**
     *原始已销售库存
     */
    private Long originSaledStockQuantity;

    /**
     * 扣减后的销售库存
     */
    private Long deductedSaleStockQuantity;

    /**
     *增加的已销售库存
     */
    private Long increasedSaledStockQuantity;

    /**
     * 状态 1 - 已扣减 2 -已释放
     */
    private Integer status;


    public static final  String ORDER_ID = "order_id";

    public static final  String SKU_CODE = "sku_code";

    public static  final String STATUS = "status";
}
