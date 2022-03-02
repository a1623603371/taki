package com.taki.order.domian.query;

import com.taki.common.core.AbstractObject;
import kotlin.Pair;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

/**
 * @ClassName OrderQuery
 * @Description 订单查询条件
 * @Author Long
 * @Date 2022/3/2 21:47
 * @Version 1.0
 */
@Data
public class OrderQuery extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -8544245732406425002L;

    private  static final Integer MAX_PAGE_SIZE = 100;

    /**
     * 业务现标识
     */
    private Integer businessIdentifier;


    /**
     * 订单类型
     */
    private Set<Integer> orderTypes;

    /**
     * 订单订单Id
     */
    private Set<String> orderIds;

    /**
     * 卖家Id
     */
    private Set<String> sellerIds;

    /**
     *父订单号
     */
    private Set<String> parentOrderIds;

    /**
     * 用户Id
     */
    private Set<String> userId;

    /**
     * 订单状态
     */
    private Set<Integer> orderStatus;

    /**
     *收货手机号
     */
    private Set<String> receiverPhones;

    /**
     * 收货人名称
     */
    private Set<String> receiverNames;

    /**
     *交易流水号
     */
    private Set<String> traderNos;

    /**
     * sku code
     */
    private Set<String> skuCodes;

    /**
     *  商品名称
     */
    private Set<String> productNames;

    /**
     * 创建时间 - 查询区间
     */
    private Pair<Date, Date> createdTimeInterval;

    /**
     *支付时间 - 查询区间
     */
    private Pair<Date,Date> payTimeInterval;

    /**
     * 支付金额 - 查询区间
     */
    private Pair<Integer,Integer> payAmountInterval;

    /**
     * 页码
     */
    private Integer pageNo = 1;

    /**
     * 页数
     */
    private Integer pageSize = 20;

}
