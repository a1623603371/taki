package com.taki.order.domian.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @ClassName OrderDeliverDetailDTO
 * @Description 订单配送详情
 * @Author Long
 * @Date 2022/3/2 22:58
 * @Version 1.0
 */
@Data
public class OrderDeliverDetailDTO extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -5867935955138531082L;

    /**
     * 订单Id
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
     * 地址
     */
    private String address;

    /**
     * 详细地址
     */
   private  String fullAddress;

    /**
     * 维度
     */
   private BigDecimal lon;

    /**
     * 经度
     */
   private BigDecimal lat;

    /**
     * 收货人名称
     */
   private  String receiverName;

    /**
     * 收货人手机
     */
   private String receiverPhone;

    /**
     * 调整地址次数
     */
    private Integer modifyAddressCount;

    /**
     * 配送员姓名
     */
    private String delivererName;
    /**
     * 配送员手机号
     */
    private String delivererPhone;

    /**
     * 出库时间
     */
    private Date outStockTime;
    /**
     * 签收时间
     */
    private Date signedTime;
}
