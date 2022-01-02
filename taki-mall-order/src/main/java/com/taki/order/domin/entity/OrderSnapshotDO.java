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
 * 订单快照表
 * </p>
 *
 * @author long
 * @since 2022-01-02
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("order_snapshot")
@ApiModel(value = "OrderSnapshotDO对象", description = "订单快照表")
public class OrderSnapshotDO extends BaseEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    @ApiModelProperty("订单号")
    @TableField("order_id")
    private String orderId;

    @ApiModelProperty("快照类型")
    @TableField("snapshot_type")
    private Integer snapshotType;

    @ApiModelProperty("订单快照内容")
    @TableField("snapshot_json")
    private String snapshotJson;

    @ApiModelProperty("创建时间")
    @TableField("gmt_create")
    private LocalDateTime gmtCreate;

    @ApiModelProperty("更新时间")
    @TableField("gmt_modified")
    private LocalDateTime gmtModified;


    public static final String ORDER_ID = "order_id";

    public static final String SNAPSHOT_TYPE = "snapshot_type";

    public static final String SNAPSHOT_JSON = "snapshot_json";

    public static final String GMT_CREATE = "gmt_create";

    public static final String GMT_MODIFIED = "gmt_modified";


}
