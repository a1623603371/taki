package com.taki.order.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName LackDTO
 * @Description TODO
 * @Author Long
 * @Date 2022/3/9 14:42
 * @Version 1.0
 */
@Data
public class LackDTO extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -2632636752192699174L;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     *售后Id
     */
    private Long afterSaleId;
}
