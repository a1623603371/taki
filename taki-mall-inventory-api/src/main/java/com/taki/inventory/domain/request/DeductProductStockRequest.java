package com.taki.inventory.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName LockProductStockRequest
 * @Description 扣减商品库存请求
 * @Author Long
 * @Date 2022/1/6 10:19
 * @Version 1.0
 */
@Data
public class DeductProductStockRequest implements Serializable {


    private static final long serialVersionUID = -7314327971204092932L;

    /**
     * 业务标识线
     */
    private Integer businessIdentifier;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 商品Id
     */
    private String productId;

    /**
     * 用户Id
     */
    private String userId;


    /**
     * 卖家Id
     */
    private String sellerId;


    /**
     * 订单条目
     */
    private List<OrderItemRequest> orderItemRequests;


    /**
     * @ClassName OrderItemRequest
     * @Description 订单条目
     * @Author Long
     * @Date 2022/1/6 10:19
     * @Version 1.0
     */
  @Data
  public  static class OrderItemRequest implements Serializable{


        private static final long serialVersionUID = 6038198605209168584L;

        /**
         * sku编码
         */
        private String skuCode;

        /**
         * 销售数量
         */
        private Integer saleQuantity;

    }
}
