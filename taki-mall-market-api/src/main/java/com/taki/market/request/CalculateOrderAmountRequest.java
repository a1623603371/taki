package com.taki.market.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName CalculateOrderAmountRequest
 * @Description 计算订单价格请求
 * @Author Long
 * @Date 2022/1/5 10:02
 * @Version 1.0
 */
@Data
public class CalculateOrderAmountRequest  extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -7021465453353456964L;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 卖家Id
     */
    private String sellerId;

    /**
     * 优惠券Id
     */
    private String couponId;

    /**
     *区域Id
     */
    private String region;

    /**
     * 订单条目信息
     */
    private List<OrderItemRequest> orderItemRequests;

    /**
     *  订单费用信息
     */
    private List<OrderAmountRequest> orderAmountRequests;


    /**
     * @ClassName CalculateOrderAmountRequest
     * @Description 订单条目信息
     * @Author Long
     * @Date 2022/1/5 10:02
     * @Version 1.0
     */
    @Data
    public static class  OrderItemRequest extends AbstractObject implements Serializable {


        private static final long serialVersionUID = -3167051873376228851L;

        /**
         * 商品Id
         */
        private String productId;

        /**
         * 商品类型
         */
        private Integer productType;

        /**
         * sku编码
         */
        private String skuCode;
        /**
         * 销售价格
         */
        private BigDecimal salePrice;

        /**
         * 销售数量
         */
        private Integer saleQuantity;

    }

    /**
     * @ClassName CalculateOrderAmountRequest
     * @Description 订单费用信息
     * @Author Long
     * @Date 2022/1/5 10:02
     * @Version 1.0
     */
    @Data
    public static class OrderAmountRequest extends AbstractObject implements Serializable{

        private static final long serialVersionUID = 7012042248040405578L;

        /**
         * 费用类型
         */
        private Integer amountType;
        /**
         * 费用
         */
        private BigDecimal amount;
    }
}
