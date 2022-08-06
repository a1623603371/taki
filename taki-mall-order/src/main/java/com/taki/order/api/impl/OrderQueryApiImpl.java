package com.taki.order.api.impl;

import com.taki.common.page.PagingInfo;
import com.taki.common.utli.ParamCheckUtil;
import com.taki.common.utli.ResponseData;
import com.taki.order.api.OrderQueryApi;
import com.taki.order.domain.dto.OrderDetailDTO;
import com.taki.order.domain.dto.OrderListDTO;
import com.taki.order.domain.query.OrderQuery;
import com.taki.order.exception.OrderBizException;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.service.OrderQueryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName OrderQueryApiImpl
 * @Description  订单查询 API
 * @Author Long
 * @Date 2022/3/2 23:19
 * @Version 1.0
 */
@DubboService(version = "1.0.0",interfaceClass =OrderQueryApi.class ,retries = 1)
@Slf4j
public class OrderQueryApiImpl implements OrderQueryApi {

    @Autowired
    private OrderQueryService orderQueryService;


    @Override
    public ResponseData<PagingInfo<OrderListDTO>> listOrders(OrderQuery orderQuery) {
        try {
            // 1.参数效验
            orderQueryService.checkQueryParam(orderQuery);

            return ResponseData.success(orderQueryService.executeListOrderQuery(orderQuery));
        }catch (OrderBizException e){
            log.error("biz error",e.getErrorMessage());
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());

        }catch (Exception e){
            log.error("error",e);
        return ResponseData.error(e.getMessage());

        }

    }

    @Override
    public ResponseData<OrderDetailDTO> orderDetail(String orderId) {
        try {
            // 检查 参数
            ParamCheckUtil.checkStringNonEmpty(orderId, OrderErrorCodeEnum.ORDER_ID_IS_NULL);
            return ResponseData.success(orderQueryService.orderDetail(orderId));
        }catch (OrderBizException e){
            log.error("biz error",e.getErrorMessage());
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());

        }catch (Exception e){
            log.error("error",e);
            return ResponseData.error(e.getMessage());

        }
    }
}
