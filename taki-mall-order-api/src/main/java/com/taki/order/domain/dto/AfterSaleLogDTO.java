package com.taki.order.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName AfterSaleLogDTO
 * @Description 售后单变更表
 * @Author Long
 * @Date 2022/3/3 22:30
 * @Version 1.0
 */
@Data
public class AfterSaleLogDTO extends AbstractObject implements Serializable {

    /**
     * 售后 Id
     */
    private String afterSaleId;

    /**
     * 前一个状态
     */
    private Integer preStatus;

    /**
     * 单前状态
     */
    private Integer currentStatus;

    /**
     * 备注
     */
    private String remark;
}
