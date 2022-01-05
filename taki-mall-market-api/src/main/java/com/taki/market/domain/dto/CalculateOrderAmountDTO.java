package com.taki.market.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName CalculateOrderAmountDTO
 * @Description 计算订单价格
 * @Author Long
 * @Date 2022/1/5 9:34
 * @Version 1.0
 */
@Data
public class CalculateOrderAmountDTO extends AbstractObject implements Serializable {


    private static final long serialVersionUID = 2789983352718596262L;


    /**
     * 订单费用信息
     */
    private List<OrderAmountDTO> orderAmountDTOList;

    /**
     * 订单条目信息
     */
    private List<OrderAmountDetailDTO> orderAmountDetailDTOList;


    /**
     * @ClassName CalculateOrderAmountDTO
     * @Description 营销计算费用
     * @Author Long
     * @Date 2022/1/5 9:34
     * @Version 1.0
     */
    @Data
    public static class OrderAmountDTO extends AbstractObject implements Serializable {


        private static final long serialVersionUID = 4025880044948870837L;

        /**
         * 订单Id
         */
        private String orderId;

        /**
         *收费类型
         */
        private Integer amountType;

        /**
         * 价格
         */
        private BigDecimal amount;
    }

    /**
     * @ClassName CalculateOrderAmountDTO
     * @Description 营销计算费用详情
     * @Author Long
     * @Date 2022/1/5 9:34
     * @Version 1.0
     */
    @Data
    public static class OrderAmountDetailDTO extends AbstractObject implements Serializable {


        private static final long serialVersionUID = 343559812774360119L;

        /**
         * 订单Id
         */
        private String orderId;

        /**
         * 商品类型
         */
        private Integer productType;

        /**
         * sku编码
         */
        private String skuCode;


        /**
         *销售 数量
         */
        private Integer saleQuantity;

        /**
         * 销售 价格
         */
        private BigDecimal salePrice;

        /**
         *收费类型
         */
        private Integer amountType;

        /**
         * 收费金额
         */
        private Integer amount;
    }
}
