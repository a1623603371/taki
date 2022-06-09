package com.taki.common.message;

import lombok.Data;
import springfox.documentation.service.ApiListing;

import java.io.Serializable;

/**
 * @ClassName ActualRefundMessage
 * @Description 售后退款消息
 * @Author Long
 * @Date 2022/3/11 17:52
 * @Version 1.0
 */
@Data
public class ActualRefundMessage  implements Serializable {

    /**
     * 售后退款Id

    private Long afterSaleRefundId;
  */
    /**
     * 售后订单Id
     */
    private String afterSaleId;

    /**
     * 订单Id
     */
    private String orderId;


    /**
     * 当前订单是否退款最后一笔
     */
    private boolean lastReturnGoods = false;

}
