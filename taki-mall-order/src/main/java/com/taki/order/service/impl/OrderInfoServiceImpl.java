package com.taki.order.service.impl;

import com.taki.common.utlis.ParamCheckUtil;
import com.taki.order.dao.OrderAutoNoDao;
import com.taki.order.domin.dto.GenOrderIdDTO;
import com.taki.order.domin.request.GenOrderIdRequest;
import com.taki.order.enums.OrderAutoTypeEnum;
import com.taki.order.manager.OrderNoManager;
import com.taki.order.mapper.OrderAutoNoMapper;
import com.taki.order.service.OrderInfoService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OrderNoManager orderNoManager;

    @Override
    public GenOrderIdDTO getGenOrderIdDTO(GenOrderIdRequest genOrderIdRequest) {

        String userId = genOrderIdRequest.getUserId();
        ParamCheckUtil.checkStringNonEmpty(userId);

        Integer businessIdentifier = genOrderIdRequest.getBusinessIdentifier();
        ParamCheckUtil.checkObjectNonNull(businessIdentifier);
        String orderId = orderNoManager.genOrderId(OrderAutoTypeEnum.SALE_ORDER.getCode(),userId);
        GenOrderIdDTO genOrderId = new GenOrderIdDTO();
        genOrderId.setOrderId(orderId);
        return genOrderId;
    }
}
