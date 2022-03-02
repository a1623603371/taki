package com.taki.order.domian.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName OrderOperateLogDTO
 * @Description 订单操作日志
 * @Author Long
 * @Date 2022/3/2 23:11
 * @Version 1.0
 */
@Data
public class OrderOperateLogDTO extends AbstractObject implements Serializable {
    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 触发来源
     */
    private Integer fromSource;

    /**
     * 前置状态
     */
    private Integer preStatus;

    /**
     * 当前状态
     */
    private Integer currentStatus;

    /**
     * 备注说明
     */
    private String remark;
}
