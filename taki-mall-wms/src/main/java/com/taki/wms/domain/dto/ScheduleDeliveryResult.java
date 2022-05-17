package com.taki.wms.domain.dto;

import com.taki.wms.domain.entity.DeliverOrderDO;
import com.taki.wms.domain.entity.DeliveryOrderItemDO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName ScheduleDeliveryResult
 * @Description 调度出库结果
 * @Author Long
 * @Date 2022/5/17 14:10
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleDeliveryResult {

    /**
     * 调度出库单
     */
    private DeliverOrderDO deliveryOrder;

    /**
     * 出库单条目
     */
    private List<DeliveryOrderItemDO> deliveryOrderItems;
}
