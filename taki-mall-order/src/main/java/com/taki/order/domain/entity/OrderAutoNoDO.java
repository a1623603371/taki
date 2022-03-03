package com.taki.order.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import io.swagger.annotations.ApiModel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @ClassName OrderAutoNo
 * @Description 订单自动生成id
 * @Author Long
 * @Date 2022/1/2 21:49
 * @Version 1.0
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("order_auto_no")
@ApiModel(value = "OrderAutoNoDO对象", description = "订单id自动生成表")
public class OrderAutoNoDO extends BaseEntity implements Serializable {
}
