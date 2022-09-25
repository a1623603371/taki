package com.taki.market.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 运费模板
 * </p>
 *
 * @author long
 * @since 2022-09-24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("market_freight_template")
public class MarketFreightTemplateDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 模板ID
     */
    @TableField("template_id")
    private String templateId;

    /**
     * 模板名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 区域ID
     */
    @TableField("region_id")
    private String regionId;

    /**
     * 标准运费
     */
    @TableField("shipping_amount")
    private BigDecimal shippingAmount;

    /**
     * 订单满多少钱则免运费
     */
    @TableField("condition_amount")
    private BigDecimal conditionAmount;


    public static final String TEMPLATE_ID = "template_id";

    public static final String NAME = "name";

    public static final String REGION_ID = "region_id";

    public static final String SHIPPING_AMOUNT = "shipping_amount";

    public static final String CONDITION_AMOUNT = "condition_amount";

}
