package com.taki.order.domian.dto;

import com.alibaba.nacos.common.NotThreadSafe;
import com.taki.common.core.AbstractObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName RemoveOrderDTO
 * @Description 删除订单
 * @Author Long
 * @Date 2022/2/26 17:40
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RemoveOrderDTO extends AbstractObject implements Serializable {

    private static final long serialVersionUID = -4410787285731003017L;

    /**
     * 删除结果
     */
    private Boolean result;
}
