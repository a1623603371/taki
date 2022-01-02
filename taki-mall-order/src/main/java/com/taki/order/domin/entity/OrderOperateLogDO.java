package com.taki.order.domin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 订单操作⽇志表
 * </p>
 *
 * @author long
 * @since 2022-01-02
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("order_operate_log")
@ApiModel(value = "OrderOperateLogDO对象", description = "订单操作⽇志表")
public class OrderOperateLogDO extends BaseEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单编号")
    @TableField("order_id")
    private String orderId;

    @ApiModelProperty("操作类型")
    @TableField("operate_type")
    private Integer operateType;

    @ApiModelProperty("前置状态")
    @TableField("pre_status")
    private Integer preStatus;

    @ApiModelProperty("当前状态")
    @TableField("current_status")
    private Integer currentStatus;

    @ApiModelProperty("备注说明")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("创建时间")
    @TableField("gmt_create")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField("gmt_modified")
    private LocalDateTime gmtModified;


    public static final String ORDER_ID = "order_id";

    public static final String OPERATE_TYPE = "operate_type";

    public static final String PRE_STATUS = "pre_status";

    public static final String CURRENT_STATUS = "current_status";

    public static final String REMARK = "remark";

    public static final String GMT_CREATE = "gmt_create";

    public static final String GMT_MODIFIED = "gmt_modified";



}
