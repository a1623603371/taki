package com.taki.fulfill.saga.impl;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.utli.ObjectUtil;
import com.taki.fulfill.converter.FulfillConverter;
import com.taki.fulfill.domain.request.ReceiveFulfillRequest;
import com.taki.fulfill.remote.WmsRemote;
import com.taki.fulfill.saga.WmsSagaService;
import com.taki.wms.domain.dto.PickDTO;

import com.taki.wms.domain.request.PickGoodsRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName WmsSagaServiceImpl
 * @Description 出库
 * @Author Long
 * @Date 2022/5/16 16:37
 * @Version 1.0
 */
@Service("wmsSageService")
@Slf4j
public class WmsSagaServiceImpl implements WmsSagaService {

    @Autowired
    private WmsRemote wmsRemote;

    @Autowired
    private FulfillConverter fulfillConverter;

    @Override
    public Boolean pickGoods(ReceiveFulfillRequest request) {
        log.info("拣货，request={}",request);

        //调用 wms 系统 发起拣货
     PickDTO pickDTO =   wmsRemote.pickGoods(buildPickGoodsRequest(request));

        log.info("拣货结果，pickDTO = {]",pickDTO);


        return true;
    }

    /**
     * @description: 构造 出库单请求
     * @param request 接收订单履约请求
     * @return
     * @author Long
     * @date: 2022/5/17 15:46
     */
    private PickGoodsRequest buildPickGoodsRequest(ReceiveFulfillRequest request) {
        PickGoodsRequest pickGoodsRequest = fulfillConverter.convertPickGoodsRequest(request);

        List<PickGoodsRequest.OrderItemRequest> itemRequests = fulfillConverter.convertPickOrderItemRequest(request.getReceiveOrderItems());


        pickGoodsRequest.setOrderItemRequests(itemRequests);

        return pickGoodsRequest;

    }

    @Override
    public Boolean pickGoodsCompensate(ReceiveFulfillRequest request) {

        log.info("补偿拣货 ，request={}", JSONObject.toJSONString(request));

        // 调用 WMS 取消 拣货
        Boolean result = wmsRemote.cancelPickGoods(request.getOrderId());

        log.info("补偿拣货结果，result={}",JSONObject.toJSONString(result));

        return true;
    }
}
