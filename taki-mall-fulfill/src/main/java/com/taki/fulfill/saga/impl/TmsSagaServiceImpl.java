package com.taki.fulfill.saga.impl;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.utlis.ObjectUtil;
import com.taki.common.utlis.ResponseData;
import com.taki.fulfill.dao.OrderFulfillDao;
import com.taki.fulfill.dao.OrderFulfillItemDao;
import com.taki.fulfill.domain.entity.OrderFulfillDO;
import com.taki.fulfill.domain.request.ReceiveFulFillRequest;
import com.taki.fulfill.exection.FulfillBizException;
import com.taki.fulfill.exection.FulfillErrorCodeEnum;
import com.taki.fulfill.saga.TmsSagaService;
import com.taki.tms.api.TmsApi;
import com.taki.tms.domain.dto.SendOutDTO;
import com.taki.tms.domain.request.SendOutRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

/**
 * @ClassName TmsSagaServiceImpl
 * @Description 物流发货
 * @Author Long
 * @Date 2022/5/17 15:57
 * @Version 1.0
 */
@Service
@Slf4j
public class TmsSagaServiceImpl implements TmsSagaService {

    @DubboReference(version = "1.0.0",retries = 0)
    private TmsApi tmsApi;

    private OrderFulfillDao orderFulfillDao;
    @Override
    public Boolean senOut(ReceiveFulFillRequest request) {
        log.info("发货，request={}", JSONObject.toJSONString(request));

        //1.调用tms进行发货
        ResponseData<SendOutDTO> result = tmsApi.sendOut(buildSendOutRequest(request));

        log.info("发货结果,result={}",JSONObject.toJSONString(result));

        if (!result.getSuccess()){

            throw new FulfillBizException(FulfillErrorCodeEnum.TMS_IS_ERROR);

        }

        //2.查询 履约单
        OrderFulfillDO orderFulfillDO = orderFulfillDao.getOne(request.getOrderId());

        //3.查询物流单好
        String logisticCode =   result.getData().getLogisticsCode();

        orderFulfillDao.saveLogisticsCode(orderFulfillDO.getFulFillId(), logisticCode);

        return true;
    }

    /**
     * @description: 构造发货请求
     * @param request 履约请求
     * @return
     * @author Long
     * @date: 2022/5/17 16:03
     */
    private SendOutRequest buildSendOutRequest(ReceiveFulFillRequest request) {
        SendOutRequest sendOutRequest = request.clone(SendOutRequest.class);

        List<SendOutRequest.OrderItemRequest> orderItemRequests  = ObjectUtil.convertList(request.getReceiveOrderItems(),SendOutRequest.OrderItemRequest.class);

        sendOutRequest.setOrderItems(orderItemRequests);

        return sendOutRequest;

    }

    @Override
    public Boolean canOutCompensate(ReceiveFulFillRequest request) {
        log.info("补偿发货，request={}",JSONObject.toJSONString(request));

        ResponseData<Boolean> result = tmsApi.cancelSendOut(request.getOrderId());

        if (!result.getSuccess()){
            throw new FulfillBizException(FulfillErrorCodeEnum.TMS_IS_ERROR);
        }
        log.info("补偿发货结果，result={}",JSONObject.toJSONString(request));


        return true;
    }
}
