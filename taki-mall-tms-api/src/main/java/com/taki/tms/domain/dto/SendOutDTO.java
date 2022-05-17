package com.taki.tms.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName SendOutDTO
 * @Description 发货
 * @Author Long
 * @Date 2022/5/17 14:54
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendOutDTO {

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 物流单号
     */
    private String logisticsCode;
}
