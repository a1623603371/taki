package com.taki.order.service.impl;

import com.taki.common.page.PagingInfo;
import com.taki.order.domain.dto.AfterSaleOrderDetailDTO;
import com.taki.order.domain.dto.AfterSaleOrderListDTO;
import com.taki.order.domain.dto.OrderLackItemDTO;
import com.taki.order.domain.query.AfterSaleQuery;
import com.taki.order.service.AfterSaleQueryService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName AfterSaleQueryServiceImpl
 * @Description 售后查询
 * @Author Long
 * @Date 2022/3/3 22:35
 * @Version 1.0
 */
@Service
public class AfterSaleQueryServiceImpl implements AfterSaleQueryService {
    @Override
    public void checkQueryParam(AfterSaleQuery afterSaleQuery) {

    }

    @Override
    public PagingInfo<AfterSaleOrderListDTO> executeListQuery(AfterSaleQuery query) {
        return null;
    }

    @Override
    public AfterSaleOrderDetailDTO afterSaleDetail(Long afterSaleId) {
        return null;
    }

    @Override
    public List<OrderLackItemDTO> getOrderLackItemInfo(String orderId) {
        return null;
    }
}
