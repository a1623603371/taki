package com.taki.market.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName FreightTemplateDO
 * @Description 运费模板
 * @Author Long
 * @Date 2022/2/18 11:07
 * @Version 1.0
 */
@Data
@TableName("market_freight_template")
public class FreightTemplateDO extends BaseEntity implements Serializable {


    private static final long serialVersionUID = 3525232887183485865L;


    /**
     * 模板名称
     */
    private String name;


    /**
     * 区域Id
     */
    private  String regionId;


    /**
     * 标注收费
     */
    private Integer shippingAmount;


    /**
     * 订单满多少则免运费
     */
    private Integer conditionAmount;
}
