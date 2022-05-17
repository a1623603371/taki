package com.taki.wms.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName PickGoodsRequest
 * @Description 拣货请求
 * @Author Long
 * @Date 2022/5/16 17:02
 * @Version 1.0
 */
@Data
@Builder
public class PickGoodsRequest extends AbstractObject implements Serializable {


    private static final long serialVersionUID = 7288585809656581768L;


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
     * 用户ID
     *
     */
    private String userId;


    /**
     * 配送类型，默认自选
     */
    private Integer deliveryType;

    /**
     * s收货人名称
     */
    private String receiverName;

    /**
     * 收货人手机
     */
    private String receiverPhone;

    /**
     * 省
     */
    private String receiverProvince;

    /**
     * 市
     */
    private String receiverCity;

    /**
     * 区
     */
    private String receiverArea;

    /**
     *街道地址
     */
    private String receiverStreet;

    /**
     * 详细地址
     */
    private String receiverDetailAddress;
    /**
     * 经度
     */
    private BigDecimal receiverLon;

    /**
     * 纬度
     */
    private BigDecimal receiverLat;

    /**
     * 用户备注
     */
    private String userRemark;
    /**
     * 支付类型
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

    /**
     * 订单明细集合
     */
    private List<OrderItemRequest> orderItemRequests;

    /**
     * 模拟wms 异常
     */
    private String wmsException;

    /**
     * 订单明细请求
     */
    @Data
    @Builder
    public  static class OrderItemRequest extends AbstractObject implements Serializable{


        private static final long serialVersionUID = -1788826954873351872L;

        /**
         * 商品 sku 编码
         */
        private String skuCode;

        /**
         * 商品名称
         */
        private String productName;

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
         * 当前商品支付原价
         */
        private BigDecimal originAmount;


        @Tolerate
        public OrderItemRequest() {

        }
    }
}
