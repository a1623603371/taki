package com.taki.order.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taki.common.page.PagingInfo;
import com.taki.common.utlis.ParamCheckUtil;
import com.taki.order.dao.OrderInfoDao;
import com.taki.order.domain.dto.OrderDetailDTO;
import com.taki.order.domain.dto.OrderListDTO;
import com.taki.order.domain.query.OrderQuery;
import com.taki.order.domain.dto.OrderListQueryDTO;
import com.taki.order.enums.BusinessIdentifierEnum;
import com.taki.order.enums.OrderStatusEnum;
import com.taki.order.enums.OrderTypeEnum;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.service.OrderQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * @ClassName OrderQueryServiceImpl
 * @Description 订单查询 service 组件
 * @Author Long
 * @Date 2022/3/2 23:24
 * @Version 1.0
 */
@Service
public class OrderQueryServiceImpl implements OrderQueryService {


    @Autowired
    private OrderInfoDao orderInfoDao;

    @Override
    public void checkQueryParam(OrderQuery query) {

        ParamCheckUtil.checkObjectNonNull(query.getBusinessIdentifier(), OrderErrorCodeEnum.BUSINESS_IDENTIFIER_ERROR);
        checkIntAllowableValues(query.getBusinessIdentifier(), BusinessIdentifierEnum.allowableValues(),"businessIdentifier");
        checkIntSetAllowableValues(query.getOrderTypes(), OrderTypeEnum.allowableValues(),"orderTypes");
        checkIntSetAllowableValues(query.getOrderStatus(), OrderStatusEnum.allowableValues(),"orderStatus");

        Integer maxSize = OrderQuery.MAX_PAGE_SIZE;
        checkSetMaxSize(query.getOrderIds(),maxSize,"orderIds");
        checkSetMaxSize(query.getSellerIds(),maxSize,"sellerIds");
        checkSetMaxSize(query.getParentOrderIds(),maxSize,"parentOrderIds");
        checkSetMaxSize(query.getReceiverNames(),maxSize,"receiverNames");
        checkSetMaxSize(query.getReceiverPhones(),maxSize,"receiverPhones");
        checkSetMaxSize(query.getTraderNos(),maxSize,"tradeNos");
        checkSetMaxSize(query.getUserIds(),maxSize,"userIds");
        checkSetMaxSize(query.getSkuCodes(),maxSize,"skuCodes");
        checkSetMaxSize(query.getProductNames(),maxSize,"productNames");
    }
    /** 
     * @description: 检测集合元素数量是否超过 最大值
     * @param params  参数集合
     * @param   maxSize 限制数量值
     * @param    paramName  参数名称
     * @return  void
     * @author Long
     * @date: 2022/3/3 16:59
     */ 
    private void checkSetMaxSize(Set<String> params, Integer maxSize, String paramName) {
        OrderErrorCodeEnum orderErrorCodeEnum = OrderErrorCodeEnum.COLLECTION_PARAM_CANNOT_BEYOND_MAX_SIZE;

        ParamCheckUtil.checkSetMaxSize(params,maxSize,orderErrorCodeEnum,paramName,maxSize);


    }

    /**
     * @description: 检测 订单类型
     * @param orderTypes 订单类型集合
     * @param allowableValues  订单类型枚举
     * @param paramName 参数名称
     * @return  void
     * @author Long
     * @date: 2022/3/3 16:53
     */
    private void checkIntSetAllowableValues(Set<Integer> orderTypes,Set<Integer>  allowableValues, String paramName) {
        OrderErrorCodeEnum orderErrorCodeEnum = OrderErrorCodeEnum.ENUM_PARAM_MUST_BE_IN_ALLOWABLE_VALUE;
        ParamCheckUtil.checkIntSetAllowableValues(orderTypes,allowableValues,orderErrorCodeEnum,paramName
                ,allowableValues);
    }

    /** 
     * @description: 检查业务标识
     * @param businessIdentifier 业务标识
     * @param values  业务线 枚举类 值
     * @param paramName 参数名称
     * @return  void
     * @author Long
     * @date: 2022/3/3 15:38
     */ 
    private void checkIntAllowableValues(Integer businessIdentifier, Set<Integer> values, String paramName) {
        OrderErrorCodeEnum orderErrorCodeEnum = OrderErrorCodeEnum.ENUM_PARAM_MUST_BE_IN_ALLOWABLE_VALUE;

        ParamCheckUtil.checkIntAllowableValues(businessIdentifier,values,
                orderErrorCodeEnum,paramName,values);
    }

    @Override
    public PagingInfo<OrderListDTO> executeListOrderQuery(OrderQuery orderQuery) {

        // 版本一 多表连接 查询 （效率低）

        // 组装业务查询数据规则
        OrderListQueryDTO orderListQuery = OrderListQueryDTO.Builder
                .Builder().copy(orderQuery)
                // 不展示无效订单
                .removeInValidStatus()
                .setPage(orderQuery)
                .build();
        // 2查询
        Page<OrderListDTO> page =  orderInfoDao.listByPage(orderListQuery);


        return null;
    }

    @Override
    public OrderDetailDTO orderDetail(String orderId) {
        return null;
    }
}
