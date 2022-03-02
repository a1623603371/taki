package com.taki.order.api;

import com.taki.common.utlis.ResponseData;
import com.taki.order.domian.dto.OrderDetailDTO;
import com.taki.order.domian.dto.OrderListDTO;
import com.taki.order.domian.query.OrderQuery;

import java.util.List;

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
    ResponseData<List<OrderListDTO>> listOrders(OrderQuery orderQuery);


    /***
     * @description: 订单详情
     * @param orderId 订单Id
     * @return 订单详情
     * @author Long
     * @date: 2022/3/2 23:18
     */
    ResponseData<OrderDetailDTO> orderDetail( String  orderId);
}
