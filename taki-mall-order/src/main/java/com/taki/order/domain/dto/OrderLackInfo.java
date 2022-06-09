package com.taki.order.domain.dto;

import com.taki.order.domain.entity.AfterSaleInfoDO;
import com.taki.order.domain.entity.AfterSaleItemDO;
import com.taki.order.domain.entity.AfterSaleRefundDO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName OrderLackInfo
 * @Description 缺品数据
 * @Author Long
 * @Date 2022/6/9 20:08
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderLackInfo {

    /**
     * 缺品售后单
     */
    private AfterSaleInfoDO lackAfterSaleOrder;

    /**
     * 缺品售后单条目
     */
    private List<AfterSaleItemDO>  afterSaleItems;

    /**
     *  售后退款单
     */
    private AfterSaleRefundDO afterSaleRefundDO;

    /**
     * 订单缺品扩展信息
     */
    private OrderExtJsonDTO lackExJson;

    /**
     * 订单Id
     */
    private String orderId;


}
