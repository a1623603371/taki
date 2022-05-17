package com.taki.tms.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName SendOutRequest
 * @Description 发货请求
 * @Author Long
 * @Date 2022/5/17 14:39
 * @Version 1.0
 */
@Data
@Builder
public class SendOutRequest extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -5678840852843396286L;

    /**
     * 业务标识线
     */
    private Integer businessIdentifier;

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
     * 配送类型，默认是自选配送
     */
    private Integer deliveryType;

    /**
     * 收货人名称
     */
    private String receiverName;

    /**
     * 收货人 手机
     */
    private String receiverPhone;

    /**
     *省
     */
    private String receiverProvince;

    /**
     * 市
     */
    private String receiverCity;

    /**
     * 区
     */
    private  String receiverArea;

    /**
     * 街道地址
     */
    private String receiverStreet;

    /**
     *详细街道
     */
    private String receiverDetailAddress;

    /**
     * 经度
     */
    private BigDecimal receiverLon;

    /**
     * 维度
     */
    private BigDecimal receiverLat;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 支付金额
     */
    private BigDecimal payAmount;

    /**
     * 总金额
     */
    private BigDecimal totalAmount;

    /**
     * 运费
     */
    private BigDecimal deliveryAmount;


    private List<OrderItemRequest> orderItems;

    /**
     * 模拟 tms 服务异常
     */
    private String tmsException;

    @Tolerate
    public SendOutRequest() {
    }

    /**
     * 订单商品明细请求
     */
    @Data
    @Builder
    public static class OrderItemRequest extends AbstractObject implements Serializable{


        private static final long serialVersionUID = 797533041878574590L;

        /**
         * 商品 sku 编码
         */
        private String skuCode;

        /**
         * 商品 名称
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
         * 支付金额
         */
        private BigDecimal payAmount;

        /**
         * 当前 商品支付原总价
         */
        private BigDecimal originAmount;

        @Tolerate
        public OrderItemRequest() {

        }
    }
}
