package com.taki.order.domain.dto;

import com.taki.order.domain.query.OrderQuery;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.Set;

/**
 * @ClassName OrderListQueryDTO
 * @Description 订单查询集合数据
 * @Author Long
 * @Date 2022/3/3 17:07
 * @Version 1.0
 */
@Data
public class OrderListQueryDTO {
    /**
     * 业务线
     */
    private Integer businessIdentifier;
    /**
     * 订单类型
     */
    private Set<Integer> orderTypes;
    /**
     * 订单号
     */
    private Set<String> orderIds;
    /**
     * 卖家ID
     */
    private Set<String> sellerIds;
    /**
     * 父订单号
     */
    private Set<String> parentOrderIds;
    /**
     * 用户ID
     */
    private Set<String> userIds;
    /**
     * 订单状态
     */
    private Set<Integer> orderStatus;
    /**
     * 收货人手机号
     */
    private Set<String> receiverPhones;
    /**
     * 收货人姓名
     */
    private Set<String> receiverNames;
    /**
     * 交易流水号
     */
    private Set<String> tradeNos;

    /**
     * sku code
     */
    private Set<String> skuCodes;
    /**
     * sku商品名称
     */
    private Set<String> productNames;
    /**
     * 创建时间-查询区间
     */
    private Pair<Date,Date> createdTimeInterval;
    /**
     * 支付时间-查询区间
     */
    private Pair<Date,Date> payTimeInterval;
    /**
     * 支付金额-查询区间
     */
    private Pair<Integer,Integer> payAmountInterval;


    /**
     * 页码
     */
    private Integer pageNo = 1;
    /**
     * 每页条数
     */
    private Integer pageSize = 20;

    /**
     * 内部构造器
     */
    public static class  Builder {

        private OrderListQueryDTO orderListQuery = null;

        public static Builder Builder() {
          return new Builder();
        }

        public  Builder  copy (OrderQuery orderQuery){
            orderListQuery = orderQuery.clone(OrderListQueryDTO.class);
            return this;
        }

        /** 
         * @description:  不展示无效订单
         * @param 
         * @return
         * @author Long
         * @date: 2022/3/3 17:13
         */ 
        public Builder removeInValidStatus(){
            if(CollectionUtils.isEmpty(orderListQuery.getOrderStatus())){
                orderListQuery.setPageNo(orderListQuery.pageNo);
                orderListQuery.setPageSize(orderListQuery.pageSize);
            }
            return this;
        }

        /**
         * @description: 设置分页
         * @param orderQuery 订单查询条件
         * @return
         * @author Long
         * @date: 2022/3/3 17:15
         */
        public Builder setPage(OrderQuery orderQuery){
            orderListQuery.setPageNo(orderListQuery.pageNo);
            orderListQuery.setPageSize(orderListQuery.pageSize);
            return this;

        }

        public OrderListQueryDTO build(){
            return orderListQuery;
        }
    }
}
