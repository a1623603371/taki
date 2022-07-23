package com.taki.order.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单配送信息	表
 * </p>
 *
 * @author long
 * @since 2022-01-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("order_delivery_detail")
public class OrderDeliveryDetailDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    //("订单编号")
    @TableField("order_id")
    private String orderId;

   //("配送类型")
    @TableField("delivery_type")
    private Integer deliveryType;

    //("省")
    @TableField("province")
    private String province;

    //("市")
    @TableField("city")
    private String city;

   //"区")
    @TableField("area")
    private String area;

    //("街道")
    @TableField("street")
    private String street;

    //("详细地址")
    @TableField("detail_address")
    private String detailAddress;

   //("经度")
    @TableField("lon")
    private BigDecimal lon;

   //("维度")
    @TableField("lat")
    private BigDecimal lat;

   //("收货⼈姓名")
    @TableField("receiver_name")
    private String receiverName;

    //("收货⼈电话")
    @TableField("receiver_phone")
    private String receiverPhone;

    //"调整地址次	数")
    @TableField("modify_address_count")
    private Integer modifyAddressCount;

   //("配送员编号")
    @TableField("deliverer_no")
    private String delivererNo;

   //("配送员姓名")
    @TableField("deliverer_name")
    private String delivererName;

    //("配送员⼿机号")
    @TableField("deliverer_phone")
    private String delivererPhone;

    //("出库时间")
    @TableField("out_stock_time")
    private LocalDateTime outStockTime;

    //("签收时间")
    @TableField("signed_time")
    private LocalDateTime signedTime;




    public static final String ORDER_ID = "order_id";

    public static final String DELIVERY_TYPE = "delivery_type";

    public static final String PROVINCE = "province";

    public static final String CITY = "city";

    public static final String AREA = "area";

    public static final String STREET = "street";

    public static final String DETAIL_ADDRESS = "detail_address";

    public static final String LON = "lon";

    public static final String LAT = "lat";

    public static final String RECEIVER_NAME = "receiver_name";

    public static final String RECEIVER_PHONE = "receiver_phone";

    public static final String MODIFY_ADDRESS_COUNT = "modify_address_count";

    public static final String DELIVERER_NO = "deliverer_no";

    public static final String DELIVERER_NAME = "deliverer_name";

    public static final String DELIVERER_PHONE = "deliverer_phone";

    public static final String OUT_STOCK_TIME = "out_stock_time";

    public static final String SIGNED_TIME = "signed_time";



}
