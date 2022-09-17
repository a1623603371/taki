package com.taki.order.domain.dto;

import com.taki.common.core.AbstractObject;
import com.taki.order.domain.request.CreateOrderRequest;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ReleaseProductStockDTO
 * @Description 释放商品库存参数
 * @Author Long
 * @Date 2022/5/13 16:53
 * @Version 1.0
 */
@Data
public class ReleaseProductStockDTO  implements Serializable {


    private static final long serialVersionUID = 4697913125587929762L;
    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 订单条目
     */
    private List<OrderItemRequest> orderItemRequests;



    @Data
    public static class OrderItemRequest  implements Serializable{


        private static final long serialVersionUID = -8072385583551384211L;

        /**
         * 商品sku 编码
         */
        private String skuCode;

        /**
         * 销售数量
         */
        private Integer saleQuantity;
    }
}
