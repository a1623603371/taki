package com.taki.inventory.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName ReleaseProductRequest
 * @Description   释放商品库存请求参数实体类
 * @Author Long
 * @Date 2022/2/17 10:17
 * @Version 1.0
 */
@Data
public class ReleaseProductStockRequest extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -6011201511453788339L;


    /**
     * 订单Id
     */
    private String orderId;



    private List<OrderItemRequest> orderItemRequests;

    /**
     * @ClassName OrderItemRequest
     * @Description  订单条目
     * @Author Long
     * @Date 2022/2/17 10:17
     * @Version 1.0
     */
    @Data
    public static  class OrderItemRequest {

        /**
         * 商品sku编号
         */
        private String skuCode;

        /**
         * 销售数量
         */
        private Integer saleQuantity;
    }
}
