package com.taki.tms.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName LogisticOrderDO
 * @Description 物流单
 * @Author Long
 * @Date 2022/5/17 14:58
 * @Version 1.0
 */
@Data
@TableName("logistic_order")
public class LogisticOrderDO extends BaseEntity  implements Serializable {


    /**
     * 业务线
     */
    private Integer businessIdentifier;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 商家Id
     */
    private String sellerId;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 物流单号
     */
    private String logisticCode;

    /**
     * 物流单内容
     */
    private String content;


    public static final  String ORDER_ID ="order_id";
}
