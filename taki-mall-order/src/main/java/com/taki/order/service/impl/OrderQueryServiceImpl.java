package com.taki.order.service.impl;

import com.taki.common.page.PagingInfo;
import com.taki.order.domian.dto.OrderDetailDTO;
import com.taki.order.domian.dto.OrderListDTO;
import com.taki.order.domian.query.OrderQuery;
import com.taki.order.service.OrderQueryService;
import org.springframework.stereotype.Service;

/**
 * @ClassName OrderQueryServiceImpl
 * @Description 订单查询 service 组件
 * @Author Long
 * @Date 2022/3/2 23:24
 * @Version 1.0
 */
@Service
public class OrderQueryServiceImpl implements OrderQueryService {
    @Override
    public void checkQueryParam(OrderQuery orderQuery) {

    }

    @Override
    public PagingInfo<OrderListDTO> executeListOrderQuery(OrderQuery orderQuery) {
        return null;
    }

    @Override
    public OrderDetailDTO orderDetail(String orderId) {
        return null;
    }
}
