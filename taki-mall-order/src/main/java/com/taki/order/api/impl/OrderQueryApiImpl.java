package com.taki.order.api.impl;

import com.taki.common.utlis.ResponseData;
import com.taki.order.api.OrderQueryApi;
import com.taki.order.domian.dto.OrderDetailDTO;
import com.taki.order.domian.dto.OrderListDTO;
import com.taki.order.domian.query.OrderQuery;
import com.taki.order.service.OrderQueryService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName OrderQueryApiImpl
 * @Description  订单查询 API
 * @Author Long
 * @Date 2022/3/2 23:19
 * @Version 1.0
 */
@DubboService(version = "1.0.0",retries = 1)
public class OrderQueryApiImpl implements OrderQueryApi {

    @Autowired
    private OrderQueryService orderQueryService;


    @Override
    public ResponseData<List<OrderListDTO>> listOrders(OrderQuery orderQuery) {
        return null;
    }

    @Override
    public ResponseData<OrderDetailDTO> orderDetail(String orderId) {
        return null;
    }
}
