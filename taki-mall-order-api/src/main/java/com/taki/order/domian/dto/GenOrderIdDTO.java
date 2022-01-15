package com.taki.order.domian.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName GenOrderIdDTO
 * @Description 订单Id 实体
 * @Author Long
 * @Date 2022/1/2 19:16
 * @Version 1.0
 */
@Data
public class GenOrderIdDTO extends AbstractObject implements Serializable {


    private static final long serialVersionUID = 5498943948263831233L;
    /**
     * 订单id
     */
    private String orderId;

}
