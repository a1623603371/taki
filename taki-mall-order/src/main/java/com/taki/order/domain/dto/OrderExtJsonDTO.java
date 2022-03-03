package com.taki.order.domain.dto;

import lombok.Data;

/**
 * @ClassName OrderExtJsonDTO
 * @Description 订单扩张字段
 * @Author Long
 * @Date 2022/3/3 21:32
 * @Version 1.0
 */
@Data
public class OrderExtJsonDTO {

    /**
     * 是否缺品 false：未缺品 true : 缺品
     */
    private Boolean lackFlag = false;

    /**
     * 订单缺品信息
     */
    private OrderLackInfoDTO lackInfoDTO;
}
