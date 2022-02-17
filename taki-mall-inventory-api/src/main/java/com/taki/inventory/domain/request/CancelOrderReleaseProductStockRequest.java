package com.taki.inventory.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName CancelOrderReleaseProductStockRequest
 * @Description 取消订单 释放商品库存请求
 * @Author Long
 * @Date 2022/2/17 10:33
 * @Version 1.0
 */
@Data
public class CancelOrderReleaseProductStockRequest extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -4740418410552795053L;

    /**
     * 订单包含 sku List
     */
    private List<String> skuCodeList;


    /**
     * 订单Id
     */
    private String orderId;


    /**
     * 业务标识线
     */
    private Integer businessIdentifier;
}
