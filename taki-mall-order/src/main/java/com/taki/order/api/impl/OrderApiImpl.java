package com.taki.order.api.impl;

import com.taki.common.page.PagingInfo;
import com.taki.common.utlis.ParamCheckUtil;
import com.taki.common.utlis.ResponseData;
import com.taki.order.api.OrderApi;
import com.taki.order.constants.OrderConstants;
import com.taki.order.domain.dto.*;
import com.taki.order.domain.query.OrderQuery;
import com.taki.order.domain.request.RemoveOrderRequest;
import com.taki.order.domain.request.*;
import com.taki.order.exception.OrderBizException;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @ClassName OrderApiImpl
 * @Description 订单运程调用接口实现
 * @Author Long
 * @Date 2022/1/15 13:20
 * @Version 1.0
 */
@Slf4j
@DubboService(version = "1.0.0",interfaceClass = OrderApi.class,retries = 0)
public class OrderApiImpl implements OrderApi {


    @Autowired
    private OrderInfoService orderInfoService;


    @Override
    public ResponseData<GenOrderIdDTO> genOrderId(GenOrderIdRequest genOrderIdRequest) {
        try {
            String userId = genOrderIdRequest.getUserId();
            if (StringUtils.isBlank(userId)){
                return ResponseData.error(OrderErrorCodeEnum.USER_ID_IS_NULL);
            }
            GenOrderIdDTO genOrderIdDTO = orderInfoService.getGenOrderId(genOrderIdRequest);
            return ResponseData.success(genOrderIdDTO);
        } catch (OrderBizException e){
            log.error("biz error",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());
        } catch (Exception e){
            log.error("system error",e);

            return ResponseData.error(e.getMessage());
        }


    }

    @Override
    @Transactional
    public ResponseData<CreateOrderDTO> createOrder(CreateOrderRequest createOrderRequest) {

        try {
        CreateOrderDTO createOrderDTO = orderInfoService.createOrder(createOrderRequest);
        return ResponseData.success(createOrderDTO);
        }catch (OrderBizException e){
            log.error("biz error",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());
        }catch (Exception e){
            log.error("system error",e);
            return ResponseData.error(e.getMessage());
        }
    }

    @Override
    public ResponseData<PrePayOrderDTO> prePayOrder(PrePayOrderRequest payOrderRequest) {

            try {
                PrePayOrderDTO prePayOrderDTO = orderInfoService.prePayOrder(payOrderRequest);
                return ResponseData.success(prePayOrderDTO);
            }catch (OrderBizException e){
                log.error("biz error",e);
                return  ResponseData.error(e.getErrorCode(),e.getErrorMessage());
            }catch (Exception e){
                log.error("system error",e);
                return ResponseData.error(e.getMessage());
            }

    }

    @Override
    public ResponseData<Boolean> payCallback(PayCallbackRequest payCallbackRequest) {

        try {
            orderInfoService.payCallback(payCallbackRequest);
            return ResponseData.success(true);
        }catch (OrderBizException e){
            log.error("biz error",e);
            return  ResponseData.error(e.getErrorCode(),e.getErrorMessage());
        }catch (Exception e){
            log.error("system error",e);
            return ResponseData.error(e.getMessage());
        }
    }

    @Override
    public ResponseData<RemoveOrderDTO> removeOrder(RemoveOrderRequest removeOrderRequest) {

        // 参数效验
        Set<String> orderIdSet = removeOrderRequest.getOrderIds();

        ParamCheckUtil.checkCollectionNonEmpty(orderIdSet,OrderErrorCodeEnum.ORDER_ID_IS_NULL);

        ParamCheckUtil.checkSetMaxSize(orderIdSet, OrderConstants.REMOVE_ORDER_COUNT,OrderErrorCodeEnum
        .COLLECTION_PARAM_CANNOT_BEYOND_MAX_SIZE,"orderIds",OrderConstants.REMOVE_ORDER_COUNT);
        List<String> orderIds = new ArrayList<>(orderIdSet);
        try {
            Boolean result = orderInfoService.removeOrders(orderIds);
            return ResponseData.success(new RemoveOrderDTO(result));
        }catch (OrderBizException e){
            log.error("biz error",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());
        }catch (Exception e){
            log.error("system error",e);
            return ResponseData.error(e.getMessage());
        }
    }

    @Override
    public ResponseData<AdjustDeliveryAddressDTO> adjustDeliveryAddress(AdjustDeliveryAddressRequest adjustDeliveryAddressRequest) {
        // 参数效应
        try {
            ParamCheckUtil.checkStringNonEmpty(adjustDeliveryAddressRequest.getOrderId(),OrderErrorCodeEnum.ORDER_ID_IS_NULL);

            // 执行调整 订单配送地址逻辑
           Boolean result =   orderInfoService.adjustDeliveryAddress(adjustDeliveryAddressRequest);

           return ResponseData.success(new AdjustDeliveryAddressDTO(result));
        }catch (OrderBizException e){
            log.error("biz error",e);
            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());
        }catch (Exception e){
            log.error("system error",e);
            return ResponseData.error(e.getMessage());
        }



    }

}
