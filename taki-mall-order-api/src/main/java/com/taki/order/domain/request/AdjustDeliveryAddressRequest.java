package com.taki.order.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName AdjustDeliveryAddressRequest
 * @Description 调证 订单配送地址请求
 * @Author Long
 * @Date 2022/2/18 10:00
 * @Version 1.0
 */
@NoArgsConstructor
@Data
public class AdjustDeliveryAddressRequest extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -6170218798395466210L;


    /**
     * 订单iD
     */
    private String orderId;

    /**
     * 省
     */
    private String province;
    /**
     * 市
     */
    private String city;

    /**
     * 区
     */
    private String area;

    /**
     * 街道
     */
    private String street;

    /**
     * 详细地址
     */
    private String detailAddress;

    /**
     * 经度
     */
    private BigDecimal lon;

    /**
     * 维度
     */
    private  BigDecimal lat;

}
