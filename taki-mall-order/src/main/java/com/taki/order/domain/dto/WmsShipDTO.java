package com.taki.order.domain.dto;

import com.taki.common.enums.OrderStatusChangEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName WmsShipDTO
 * @Description 物流配送结果请求
 * @Author Long
 * @Date 2022/4/6 14:33
 * @Version 1.0
 */
@Data
public class WmsShipDTO {

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 订单状态变更
     */
    private OrderStatusChangEnum statusChang;


    /**
     * 出库时间
     */
    private LocalDateTime outStockTime;

    /**
     * 签收时间
     */
    private LocalDateTime signedTime;

    /**
     * 配送员 code
     */
    private String delivererNo;

    /**
     * 配送员名称
     */
    private String deliverName;


    /**
     * 配送员手机号
     */
    private String delivererPhone;

}
