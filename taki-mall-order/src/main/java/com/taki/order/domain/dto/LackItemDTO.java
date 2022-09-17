package com.taki.order.domain.dto;

import com.taki.common.core.AbstractObject;
import com.taki.order.domain.entity.OrderItemDO;
import com.taki.product.domian.dto.ProductSkuDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName LackItemDTO
 * @Description 缺品DTO
 * @Author Long
 * @Date 2022/3/10 17:31
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LackItemDTO  implements Serializable {

    /**
     *缺品订单条目
     */
    private OrderItemDO orderItem;

    /**
     * 缺品数量
     */
    private Integer lackNum;


    private ProductSkuDTO productSkuDTO;

}
