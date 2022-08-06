package com.taki.order.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import java.io.Serializable;

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
@TableName("order_snapshot")
public class OrderSnapshotDO extends BaseEntity implements Serializable{

    private static final long serialVersionUID = 1L;

    //"订单号"
    @TableField("order_id")
    private String orderId;

    //"快照类型"
    @TableField("snapshot_type")
    private Integer snapshotType;

   //"订单快照内容"
    @TableField("snapshot_json")
    private String snapshotJson;



    public static final String ORDER_ID = "order_id";

    public static final String SNAPSHOT_TYPE = "snapshot_type";

    public static final String SNAPSHOT_JSON = "snapshot_json";



}
