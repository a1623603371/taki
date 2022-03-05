package com.taki.fulfill.domain.evnet;

import lombok.Data;

/**
 * @ClassName OrderDeliveredWmsEvent
 * @Description 订单已配送物流结果 消息
 * @Author Long
 * @Date 2022/3/5 11:41
 * @Version 1.0
 */
@Data
public class OrderDeliveredWmsEvent  extends   BaseWmsShipEvent{

    /**
     *配送人员 code
     */
    private String delivererNo;

    /**
     * 配送人员 姓名
     */
    private String  delivererName;

    /**
     * 配送人 手机
     */
    private String delivererPhone;
}
