package com.taki.fulfill.saga.impl;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.utli.ObjectUtil;
import com.taki.fulfill.converter.FulfillConverter;
import com.taki.fulfill.dao.OrderFulfillDao;
import com.taki.fulfill.domain.entity.OrderFulfillDO;
import com.taki.fulfill.domain.request.ReceiveFulfillRequest;
import com.taki.fulfill.remote.TmsRemote;
import com.taki.fulfill.saga.TmsSagaService;
import com.taki.tms.domain.dto.SendOutDTO;
import com.taki.tms.domain.request.SendOutRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName TmsSagaServiceImpl
 * @Description 物流发货
 * @Author Long
 * @Date 2022/5/17 15:57
 * @Version 1.0
 */
@Service("tmsSagaService")
@Slf4j
public class TmsSagaServiceImpl implements TmsSagaService {

    @Autowired
    private TmsRemote tmsRemote;
    @Autowired
    private OrderFulfillDao orderFulfillDao;


    @Autowired
    private FulfillConverter fulfillConverter;
    @Override
    public Boolean sendOut(ReceiveFulfillRequest request) {
        log.info("发货，request={}", JSONObject.toJSONString(request));

        //1.调用tms进行发货
        SendOutDTO sendOut = tmsRemote.sendOut(buildSendOutRequest(request));

        log.info("发货结果,SendOutDTO={}",JSONObject.toJSONString(sendOut));


        //2.查询 履约单
        OrderFulfillDO orderFulfillDO = orderFulfillDao.getOne(request.getOrderId());

        //3.查询物流单好
        String logisticCode =  sendOut.getLogisticsCode();

        orderFulfillDao.saveLogisticsCode(orderFulfillDO.getFulfillId(), logisticCode);

        return true;
    }

    /**
     * @description: 构造发货请求
     * @param request 履约请求
     * @return
     * @author Long
     * @date: 2022/5/17 16:03
     */
    private SendOutRequest buildSendOutRequest(ReceiveFulfillRequest request) {
        SendOutRequest sendOutRequest = fulfillConverter.convertReceiveFulfillRequest(request);

        List<SendOutRequest.OrderItemRequest> orderItemRequests = fulfillConverter.convertSendOutOrderItemRequest(request.getReceiveOrderItems());

        sendOutRequest.setOrderItems(orderItemRequests);

        return sendOutRequest;

    }

    @Override
    public Boolean sendOutCompensate(ReceiveFulfillRequest request) {
        log.info("补偿发货，request={}",JSONObject.toJSONString(request));

        Boolean result = tmsRemote.cancelSendOut(request.getOrderId());

        log.info("补偿发货结果，result={}",result);


        return true;
    }
}
