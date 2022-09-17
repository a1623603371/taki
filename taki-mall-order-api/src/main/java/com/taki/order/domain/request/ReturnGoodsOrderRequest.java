package com.taki.order.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ReturnGoodsOrderRequest
 * @Description TODO
 * @Author Long
 * @Date 2022/4/3 15:26
 * @Version 1.0
 */
@Data
public class ReturnGoodsOrderRequest  implements Serializable {


    private static final long serialVersionUID = 4785122578580142791L;

    /**
     * 订单Id
     */
    private String orderId;


    /**
     * 业务线
     */
    private Integer businessIdentifier;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 退货选项
     */
    private Integer returnGoodsCode;


    /**
     * 退货说明
     */
    private String returnGoodsDesc;

    /**
     * sku 编号
     */
    private String skuCode;
}
