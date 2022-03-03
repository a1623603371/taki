package com.taki.order.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName OrderSnapshotDTO
 * @Description 订单快照
 * @Author Long
 * @Date 2022/3/2 23:13
 * @Version 1.0
 *
 */
@Data
public class OrderSnapshotDTO extends AbstractObject implements Serializable {
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
