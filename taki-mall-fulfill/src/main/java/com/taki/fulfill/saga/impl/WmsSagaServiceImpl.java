package com.taki.fulfill.saga.impl;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.utlis.ObjectUtil;
import com.taki.common.utlis.ResponseData;
import com.taki.fulfill.domain.request.ReceiveFulFillRequest;
import com.taki.fulfill.exection.FulfillBizException;
import com.taki.fulfill.exection.FulfillErrorCodeEnum;
import com.taki.fulfill.remote.WmsRemote;
import com.taki.fulfill.saga.WmsSagaService;
import com.taki.wms.api.WmsApi;
import com.taki.wms.domain.dto.PickDTO;

import com.taki.wms.domain.request.PickGoodsRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.checkerframework.checker.units.qual.A;
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
@Service
@Slf4j
public class WmsSagaServiceImpl implements WmsSagaService {

    @Autowired
    private WmsRemote wmsRemote;

    @Override
    public Boolean pickGoods(ReceiveFulFillRequest request) {
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
    private PickGoodsRequest buildPickGoodsRequest(ReceiveFulFillRequest request) {
        PickGoodsRequest pickGoodsRequest = request.clone(PickGoodsRequest.class);

        List<PickGoodsRequest.OrderItemRequest> itemRequests = ObjectUtil
                .convertList(request.getReceiveOrderItems(), PickGoodsRequest.OrderItemRequest.class);

        pickGoodsRequest.setOrderItemRequests(itemRequests);

        return pickGoodsRequest;

    }

    @Override
    public Boolean pickGoodsCompensate(ReceiveFulFillRequest request) {

        log.info("补偿拣货 ，request={}", JSONObject.toJSONString(request));

        // 调用 WMS 取消 拣货
        Boolean result = wmsRemote.cancelPickGoods(request.getOrderId());

        log.info("补偿拣货结果，result={}",JSONObject.toJSONString(result));

        return true;
    }
}
