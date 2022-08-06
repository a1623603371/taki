package com.taki.fulfill.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @ClassName OrderFulfillDO
 * @Description 订单履约表
 * @Author Long
 * @Date 2022/5/15 15:56
 * @Version 1.0
 */
@Data
@TableName("order_fulfill")
public class OrderFulfillDO extends BaseEntity implements Serializable {


    private static final long serialVersionUID = 4894445344193244595L;


    /**
     * 业务标识线
     */
    private Integer businessIdentifier;

    /**
     * 履约单Id
     */
    private String fulfillId;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 商家Id
     */
    private String sellerId;

    /**
     * 用户Id
     */
    private String userId;


    /**
     * 配送类型，默认自选择
     */
    private Integer deliveryType;


    /**
     * 收件人名称
     */
    private String receiverName;

    /**
     *收件人 手机
     */
    private String receiverPhone;

    /**
     *省
     */
    private String receiverProvince;

    /**
     * 区
     */
    private String receiverArea;

    /**
     * 街道地址
     */
    private String receiverStreet;

    /**
     * 详细地址
     */
    private String receiverDetailAddress;

    /**
     * 经度 六位数小数
     */
    private BigDecimal receiverLon;

    /**
     * 维度 六位小数
     */
    private BigDecimal receiverLat;


    /**
     * 配送员编号
     */
    private String  delivererNo;

    /**
     * 配送人员 名称
     */
    private String delivererName;

    /**
     * 物流单号
     */
    private String logisticsCode;

    /**
     * 用户备注
     */
    private String userRemark;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 总支付金额
     */
    private BigDecimal totalAmount;

    /**
     *运费
     */
    private Integer deliveryAmount;


    public static final  String FULFILL_ID = "fulfill_Id";

    public static  final  String LOGISTICS_CODE = "logistics_code";


    public static final  String ORDER_ID = "order_id";


    public static final String DELIVERER_NO  = "deliverer_no";

    public  static  final  String DELIVERER_NAME = "deliverer_name";

    public  static final  String  DELIVERER_PHONE = "deliverer_phone";

}




