package com.taki.market.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author long
 * @since 2022-09-24
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("market_promotion")
public class MarketPromotionDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 促销活动名称
     */
    @TableField("`name`")
    private String name;

    /**
     * 开始时间
     */
    @TableField("start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @TableField("end_time")
    private LocalDateTime endTime;

    /**
     * 通知类型
     */
    @TableField("inform_type")
    private Integer informType;

    /**
     * 促销活动说明备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 促销活动的状态，1：启用，2：停用
     */
    @TableField("`status`")
    private Integer status;

    /**
     * 促销活动类型，1满减，2折扣，3优惠券，4会员积分
     */
    @TableField("`type`")
    private Integer type;

    /**
     * 促销活动的规则
     */
    @TableField("rule")
    private String rule;


    public static final String NAME = "name";

    public static final String START_TIME = "start_time";

    public static final String END_TIME = "end_time";

    public static final String INFORM_TYPE = "inform_type";

    public static final String REMARK = "remark";

    public static final String STATUS = "status";

    public static final String TYPE = "type";

    public static final String RULE = "rule";

}
