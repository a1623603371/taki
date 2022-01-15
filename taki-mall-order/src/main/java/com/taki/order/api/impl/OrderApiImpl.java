package com.taki.order.api.impl;

import com.taki.common.utlis.ResponseData;
import com.taki.order.api.OrderApi;
import com.taki.order.domian.dto.CreateOrderDTO;
import com.taki.order.domian.dto.GenOrderIdDTO;
import com.taki.order.domian.request.CreateOrderRequest;
import com.taki.order.domian.request.GenOrderIdRequest;
import com.taki.order.exception.OrderBizException;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.service.OrderInfoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

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
            GenOrderIdDTO genOrderIdDTO = orderInfoService.getGenOrderIdDTO(genOrderIdRequest);
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
}
