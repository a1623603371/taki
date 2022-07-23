package com.taki.order.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import java.io.Serializable;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
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
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderOperateLogDO extends BaseEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    //("订单编号")
    @TableField("order_id")
    private String orderId;

   //("操作类型")
    @TableField("operate_type")
    private Integer operateType;

    //("前置状态")
    @TableField("pre_status")
    private Integer preStatus;

    //("当前状态")
    @TableField("current_status")
    private Integer currentStatus;

    //("备注说明")
    @TableField("remark")
    private String remark;




    public static final String ORDER_ID = "order_id";

    public static final String OPERATE_TYPE = "operate_type";

    public static final String PRE_STATUS = "pre_status";

    public static final String CURRENT_STATUS = "current_status";

    public static final String REMARK = "remark";





}
