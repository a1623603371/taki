package com.taki.order.service.impl;

import com.taki.common.utlis.ParamCheckUtil;
import com.taki.order.domin.dto.GenOrderIdDTO;
import com.taki.order.domin.request.GenOrderIdRequest;
import com.taki.order.service.OrderInfoService;
import org.springframework.stereotype.Service;

/**
 * @ClassName OrderInfoServiceImpl
 * @Description 订单 service接口实现
 * @Author Long
 * @Date 2022/1/2 17:07
 * @Version 1.0
 */
@Service
public class OrderInfoServiceImpl implements OrderInfoService {


    @Override
    public GenOrderIdDTO getGenOrderIdDTO(GenOrderIdRequest genOrderIdRequest) {

        String userId = genOrderIdRequest.getUserId();
        ParamCheckUtil.checkStringNonEmpty(userId);

        Integer businessIdentifier = genOrderIdRequest.getBusinessIdentifier();
        ParamCheckUtil.checkObjectNonNull(businessIdentifier);

        String orderId = "";

        GenOrderIdDTO genOrderId = new GenOrderIdDTO();

        genOrderId.setOrderId(orderId);



        return genOrderId;
    }
}
