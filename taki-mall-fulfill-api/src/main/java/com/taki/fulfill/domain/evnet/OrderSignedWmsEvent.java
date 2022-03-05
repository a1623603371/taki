package com.taki.fulfill.domain.evnet;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName OrderSignedWmsEvent
 * @Description 订单已签收物流结果消息
 * @Author Long
 * @Date 2022/3/5 11:51
 * @Version 1.0
 */
@Data
public class OrderSignedWmsEvent extends BaseWmsShipEvent {

    /**
     * 签收事件事件
     */
    private LocalDateTime signedTime;
}
