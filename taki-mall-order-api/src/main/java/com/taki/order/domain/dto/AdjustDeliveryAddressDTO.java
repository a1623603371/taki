package com.taki.order.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName AdjustDeliveryAddressDTO
 * @Description
 * @Author Long
 * @Date 2022/2/26 19:42
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdjustDeliveryAddressDTO extends AbstractObject implements Serializable {


    private static final long serialVersionUID = 1597034550960264745L;

    private Boolean result;
}
