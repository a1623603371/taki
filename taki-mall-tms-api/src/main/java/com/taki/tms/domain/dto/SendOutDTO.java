package com.taki.tms.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName SendOutDTO
 * @Description 发货
 * @Author Long
 * @Date 2022/5/17 14:54
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendOutDTO extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -552060326868891630L;
    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 物流单号
     */
    private String logisticsCode;
}
