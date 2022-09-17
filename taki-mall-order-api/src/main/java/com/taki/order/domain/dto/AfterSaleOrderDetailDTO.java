package com.taki.order.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName AfterSaleOrderDetailDTO
 * @Description 售后订单详情DTO
 * @Author Long
 * @Date 2022/3/3 22:28
 * @Version 1.0
 */
@Data
@Builder
public class AfterSaleOrderDetailDTO  implements Serializable {


    private static final long serialVersionUID = 6320291540791127904L;
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

    /**
     * 售后单日志
     */
    private List<AfterSaleLogDTO> afterSaleLogs;
}
