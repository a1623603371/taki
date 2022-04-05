package com.taki.order.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName AfterSaleLogDO
 * @Description TODO
 * @Author Long
 * @Date 2022/4/4 14:44
 * @Version 1.0
 */
@Data
@TableName("after_sale_log")
public class AfterSaleLogDO extends BaseEntity implements Serializable {


    private static final long serialVersionUID = -763207797062153791L;

    /**
     * 售后单号
     */
    private Long afterSaleId;

    /**
     * 前一个状态
     */
    private Integer preStatus;

    /**
     * 当前状态
     */
    private Integer currentStatus;

    /**
     * 备注
     */
    private String remark;

    public static final  String AFTER_SALE_ID =  "after_sale_id";

}
