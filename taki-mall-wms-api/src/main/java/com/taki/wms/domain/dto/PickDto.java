package com.taki.wms.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName PickDto
 * @Description TODO
 * @Author Long
 * @Date 2022/5/16 17:01
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PickDto extends AbstractObject implements Serializable {


    private static final long serialVersionUID = 7224981348870745138L;

    /**
     * 订单 id
     */
    private String orderId;
}
