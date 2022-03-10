package com.taki.order.domain.dto;

import com.taki.common.core.AbstractObject;
import javafx.util.Pair;
import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * @ClassName AfterSaleListQueryDTO
 * @Description 订单列表查询入参DTO
 * @Author Long
 * @Date 2022/3/9 16:11
 * @Version 1.0
 */
@Data
public class AfterSaleListQueryDTO extends AbstractObject {

    /**
     * 业务标识线
     */
    private Integer businessIdentifier;

    /**
     * 订单类型
     */
    private Set<Integer> orderTypes;

    /**
     * 售后单状态
     */
    private Set<Integer> afterSaleStatus;

    /**
     * 售后申请来源
     */
    private Set<Integer> applySources;

    /**
     *售后类型
     */
    private Set<Integer> afterSaleTypes;

    /**
     *售后单号
     */
    private Set<Long> afterSaleIds;


    /**
     * 订单号
     */
    private Set<String> orderIds;

    /**
     * 用户Id
     */
    private Set<String> userIds;

    /**
     * sku code
     */
    private Set<String> skuCodes;

    /**
     * 创建时间- 查询区间
     */
    private Pair<Date,Date> createTimeInterval;

    /**
     * 售后申请时间 - 查询区间
     */
    private Pair<Date,Date> applyTimeInterval;

    /**
     * 售后客户审核时间 - 查询区间
     */
    private Pair<Date,Date> reviewTimeInterval;

    /**
     * 退款支付时间 - 查询区间
     */
    private Pair<Date,Date> refundPayTimeInterval;

    /**
     * 退款金额 -查询区间
     */
    private Pair<Date,Date> refundAmountInterval;


    /**
     * 页码：默认是1
     */
    private Integer pageNo = 1;


    /**
     * 页数： 默认为 20
     */
    private Integer pageSize = 20;


}
