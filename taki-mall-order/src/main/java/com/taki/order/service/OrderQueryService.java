package com.taki.order.service;

import com.taki.common.page.PagingInfo;
import com.taki.order.domain.dto.OrderDetailDTO;
import com.taki.order.domain.dto.OrderListDTO;
import com.taki.order.domain.query.OrderQuery;

/**
 * @ClassName OrderQueryService
 * @Description 订单查询 service接口
 * @Author Long
 * @Date 2022/3/2 23:20
 * @Version 1.0
 */
public interface OrderQueryService {
    /** 
     * @description:  检查查询条件
     * @param orderQuery 订单查询条件
     * @return  void
     * @author Long
     * @date: 2022/3/2 23:21
     */ 
    void checkQueryParam(OrderQuery orderQuery);

    /** 
     * @description:   执行查询
     * @param orderQuery 订单查询
     * @return  订单列表
     * @author Long
     * @date: 2022/3/2 23:23
     */ 
    PagingInfo<OrderListDTO> executeListOrderQuery(OrderQuery orderQuery);

    /***
     * @description:  查询订单详情信息
     * @param orderId 订单Id
     * @return  订单详情信息
     * @author Long
     * @date: 2022/3/2 23:24
     */
    OrderDetailDTO orderDetail(String orderId);
}
