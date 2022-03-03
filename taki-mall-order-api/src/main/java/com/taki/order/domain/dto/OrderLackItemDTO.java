package com.taki.order.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName OrderLackItemDTO
 * @Description 订单缺品
 * @Author Long
 * @Date 2022/3/2 23:14
 * @Version 1.0
 */
@Data
public class OrderLackItemDTO extends AbstractObject implements Serializable {
    /**
     * 售后信息
     */
    private AfterSaleInfoDTO afterSaleInfo;

    /**
     * 售后单条目
     */
    private List<AfterSaleItemDTO> afterSaleItems;

    /**
     * 售后支付信息
     */
    private List<AfterSalePayDTO> afterSalePays;
}
