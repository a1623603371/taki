package com.taki.fulfill.domain.evnet;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @ClassName OrderOutStockWmsEvent
 * @Description 订单已出库物流结果消息
 * @Author Long
 * @Date 2022/3/5 11:17
 * @Version 1.0
 */
@Data
public class OrderOutStockWmsEvent extends BaseWmsShipEvent {

    @DateTimeFormat(pattern = "yyyy-MM-dd  HH:mm:ss")
    private LocalDateTime outStockTime;
}
