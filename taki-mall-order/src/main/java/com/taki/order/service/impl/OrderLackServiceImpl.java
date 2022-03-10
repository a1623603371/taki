package com.taki.order.service.impl;

import com.taki.common.utlis.ParamCheckUtil;
import com.taki.order.dao.AfterSaleInfoDao;
import com.taki.order.dao.OrderInfoDao;
import com.taki.order.dao.OrderItemDao;
import com.taki.order.domain.dto.CheckLackDTO;
import com.taki.order.domain.dto.LackDTO;
import com.taki.order.domain.request.LackRequest;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.service.OrderLackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName OrderLackServiceImpl
 * @Description 订单缺品 service
 * @Author Long
 * @Date 2022/3/9 15:29
 * @Version 1.0
 */
@Slf4j
@Service
public class OrderLackServiceImpl implements OrderLackService {


    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private AfterSaleInfoDao afterSaleInfoDao;

    @Override
    public CheckLackDTO checkRequest(LackRequest lackRequest) {
        //1.参数基本校应
        ParamCheckUtil.checkStringNonEmpty(lackRequest.getOrderId(), OrderErrorCodeEnum.ORDER_ID_IS_NULL);
        ParamCheckUtil.checkCollectionNonEmpty(lackRequest.getLackItems(),OrderErrorCodeEnum.LACK_ITEM_IS_NULL);

        //

        return null;
    }

    @Override
    public LackDTO executeLackRequest(LackRequest lackRequest, CheckLackDTO checkLack) {
        return null;
    }
}
