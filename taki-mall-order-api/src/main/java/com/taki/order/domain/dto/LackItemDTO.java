package com.taki.order.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName LackItemDTO
 * @Description 缺品DTO
 * @Author Long
 * @Date 2022/3/10 17:31
 * @Version 1.0
 */
@Data
public class LackItemDTO extends AbstractObject implements Serializable {

    /**
     *缺品订单条目
     */
    private OrderItemDTO orderItem;

    /**
     * 缺品数量
     */
    private Integer lackNum;


    private ProductSkuDTO productSkuDTO;

}
