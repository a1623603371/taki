package com.taki.order.domain.entity;

import com.taki.common.core.AbstractObject;
import com.taki.common.domin.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @ClassName AfterSaleInfoDO
 * @Description 售后
 * @Author Long
 * @Date 2022/3/9 15:34
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AfterSaleInfoDO extends BaseEntity implements Serializable {


    private static final long serialVersionUID = -7430500682018238544L;


    /**
     * 售后Id
     */
    private String afterSaleId;

    /**
     * 业务线
     */
    private Integer businessIdentifier;

    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 订单来源渠道
     */
    private Integer orderSourceChannel;

    /**
     * 用户Id
     */
    private String userId;


    /**
     * 订单类型
     */
    private Integer orderType;


    /**
     * 申请售后来源
     */
    private Integer applySource;

    /**
     * 申请售后来源
     */
    private LocalDateTime applyTime;

    /**
     *申请原因编码
     */
    private Integer applyReasonCode;

    /**
     * 申请原因
     */
    private String applyReason;

    /**
     * 审核时间
     */
    private LocalDateTime reviewTime;

    /**
     * 客服审核来源
     */
    private  Integer reviewSource;

    /**
     * 客服审核结果编码
     */
    private Integer reviewReasonCode;

    /**
     * 客服审核结果
     */
    private Integer reviewReason;

    /**
     * 售后类型
     */
    private Integer afterSaleType;

    /**
     * 售后类型详情
     */
    private  Integer afterSaleTypeDetail;

    /**
     * 售后状态
     */
    private Integer afterSaleStatus;

    /**
     * 申请退款金额
     */
    private BigDecimal applyRefundAmount;

    /**
     * 实际退款金额
     */
    private BigDecimal realRefundAmount;

    /**
     * 备注
     */
    private String remark;



    public static final String AFTER_SALE_ID = "after_sale_id";

    public static  final  String ORDER_ID ="order_id";

    public  static  final  String AFTER_SALE_TYPE_DETAIL = "after_sale_type_detail";

    public static final String AFTER_SALE_STATUS ="after_sale_status";


    public static final String REVIEW_REASON = "review_reason";

    public static final  String REVIEW_REASON_CODE = "review_reason_code";


    public static final  String REVIEW_SOURCE = "review_source";

    public static final  String  REVIEW_TIME = "review_time";
}
