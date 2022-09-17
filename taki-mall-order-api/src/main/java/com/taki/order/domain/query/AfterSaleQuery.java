package com.taki.order.domain.query;

import com.taki.common.core.AbstractObject;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @ClassName AfterSaleQuery
 * @Description 售后查询
 * @Author Long
 * @Date 2022/3/3 22:14
 * @Version 1.0
 */
@Data
public class AfterSaleQuery   implements Serializable {


    private static final long serialVersionUID = 8963993635518744915L;

    public static final Integer MAX_PAGE_SIZE = 100;

    /**
     * 业务线
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
     * 售后类型
     */
    private Set<Integer> afterSaleTypes;
    /**
     * 售后单号
     */
    private Set<Long> afterSaleIds;
    /**
     * 订单号
     */
    private Set<String> orderIds;
    /**
     * 用户ID
     */
    private Set<String> userIds;
    /**
     * sku code
     */
    private Set<String> skuCodes;
    /**
     * 创建时间-查询区间
     */
    private Pair<Date,Date> createdTimeInterval;
    /**
     * 售后申请时间-查询区间
     */
    private Pair<Date,Date> applyTimeInterval;
    /**
     * 售后客服审核时间-查询区间
     */
    private Pair<Date,Date> reviewTimeInterval;
    /**
     * 退款支付时间-查询区间
     */
    private Pair<Date,Date> refundPayTimeInterval;
    /**
     * 退款金额-查询区间
     */
    private Pair<Date,Date> refundAmountInterval;
    /**
     * 页码；默认为1；
     */
    private Integer pageNo = 1;
    /**
     * 一页的数据量. 默认为20
     */
    private Integer pageSize = 20;

}
