package com.taki.order.domain.dto;

import org.apache.commons.lang3.tuple.Pair;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
@AllArgsConstructor
@NoArgsConstructor
public class AfterSaleListQueryDTO  {

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


    /**
     * 内部构造器
     */
//    public static class Builder {
//
//        private AfterSaleListQueryDTO  query = null;
//
//       public static  Builder builder(){
//           return new Builder();
//       }
//
//        public Builder copy(AfterSaleQuery afterSaleQuery){
//            query = afterSaleQuery.clone(AfterSaleListQueryDTO.class);
//            return this;
//        }
//
//        /**
//         * @description:  售后列表只展示用户主动发起的售后退款记录，超时自动取消和用户手动取消的售后默认不展示
//         * @param
//         * @return  com.taki.order.domain.dto.AfterSaleListQueryDTO.Builder
//         * @author Long
//         * @date: 2022/4/4 18:42
//         */
//        public  Builder useApplySource(){
//            if (CollectionUtils.isEmpty(query.getApplySources())){
//                query.setApplySources(AfterSaleApplySourceEnum.userApply());
//            }
//            return this;
//        }
//
//        public Builder setPage(AfterSaleQuery afterSaleQuery){
//            query.setPageNo(afterSaleQuery.getPageNo());
//            query.setPageSize(afterSaleQuery.getPageSize());
//            return this;
//        }
//
//        public  AfterSaleListQueryDTO build(){
//            return query;
//        }
//
//    }
}
