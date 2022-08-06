package com.taki.order.api;

import com.taki.common.page.PagingInfo;
import com.taki.common.utli.ResponseData;
import com.taki.order.domain.dto.OrderDetailDTO;
import com.taki.order.domain.dto.OrderListDTO;
import com.taki.order.domain.query.OrderQuery;

/**
 * @ClassName OrderQueryApi
 * @Description 订单中心 - 订单查询业务接口
 * @Author Long
 * @Date 2022/3/2 22:10
 * @Version 1.0
 */
public interface OrderQueryApi {

    /** 
     * @description:  查询 订单 列表
     * @param orderQuery 订单查询
     * @return  订单 列表
     * @author Long
     * @date: 2022/3/2 22:12
     */ 
    ResponseData<PagingInfo<OrderListDTO>> listOrders(OrderQuery orderQuery);


    /***
     * @description: 订单详情
     * @param orderId 订单Id
     * @return 订单详情
     * @author Long
     * @date: 2022/3/2 23:18
     */
    ResponseData<OrderDetailDTO> orderDetail( String  orderId);
}
