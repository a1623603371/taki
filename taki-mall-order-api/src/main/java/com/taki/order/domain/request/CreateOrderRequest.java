package com.taki.order.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName CreateOrderRequest
 * @Description 创建订单请求
 * * @Author Long
 * @Date 2022/1/3 0:16
 * @Version 1.0
 */
@Data
public class CreateOrderRequest extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -397550716343729406L;

    /**
     *订单号
     */
    private String orderId;

    /**
     * 业务线标识
     */
    private Integer businessIdentifier;

    /**
     *用户信息
     * 微信opendId
     */
    private String openId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 订单类型
     */
    private Integer orderType;

    /**
     *卖家Id
     */
    private String sellerId;

    /**
     * 用户备注
     */
    private String userRemark;


    /**
     *  优惠券Id
     */
    private String couponId;


    /**
     *送货方式
     */
    private Integer deliveryType;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     *区
     */
    private String area;

    /**
     * 街道
     */
    private String street;

    /**
     * 地址详情
     */
    private String detailAddress;

    /**
     *精度
     */
    private BigDecimal lon;

    /**
     *维度
     */
    private BigDecimal lat;


    /**
     * 收货人名称
     */
    private String receiverName;

    /**
     * 收货人手机号
     */
    private String receiverPhone;

    /**
     * 收货人地址id
     */
    private String userAddressId;
    /**
     * 地区编码
     */
    private String addressCode;
    /**
     *区域ID
     */
    private String regionId;

    /**
     *配送区域Id
     */
    private String shippingAreaId;

    /**
     * 客户端ip
     */
    private String clientIp;
    /**
     *设备编号
     */
    private String deviceId;
    /**
     * 订单商品信息
     */
    private List<OrderItemRequest> orderItemRequests;


    /**
     * 订单费用信息
     */
    private List<OrderAmountRequest> orderAmountRequests;


    /**
     * 支付信息
     */
    private List<PaymentRequest> paymentRequests;

    /**
     * @ClassName CreateOrderRequest
     * @Description 订单商品信息
     * * @Author Long
     * @Date 2022/1/3 0:16
     * @Version 1.0
     */
    @Data
    public static class OrderItemRequest extends AbstractObject implements Serializable {

        private static final long serialVersionUID = -8596245154280361909L;

        /**
         * 商品类型
         */
        private Integer productType;

        /**
         * 销售数量
         */
        private Integer saleQuantity;

        /**
         * sku 编码
         */
        private String skuCode;
    }

    /**
     * @ClassName CreateOrderRequest
     * @Description 订单费用信息
     * * @Author Long
     * @Date 2022/1/3 0:16
     * @Version 1.0
     */
    @Data
    public static class OrderAmountRequest extends AbstractObject implements Serializable {


        private static final long serialVersionUID = -8373432955221462266L;

        /**
         *费用类型
         */
        private Integer amountType;

        /**
         * 费用
         */
        private BigDecimal amount;
    }

    /**
     * @ClassName CreateOrderRequest
     * @Description 支付信息
     * * @Author Long
     * @Date 2022/1/3 0:16
     * @Version 1.0
     */
    @Data
    public static  class PaymentRequest extends AbstractObject implements Serializable {

        private static final long serialVersionUID = -2437895415443353399L;

        /**
         * 支付类型
         */
        private Integer payType;

        /**
         *账户类型
         */
        private Integer accountType;
    }


}