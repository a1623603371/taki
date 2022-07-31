package com.taki.wms.api.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.taki.common.utlis.ObjectUtil;
import com.taki.common.utlis.RandomUtil;
import com.taki.common.utlis.ResponseData;
import com.taki.wms.api.WmsApi;
import com.taki.wms.dao.DeliverOrderDao;
import com.taki.wms.dao.DeliveryOrderItemDao;
import com.taki.wms.domain.dto.PickDTO;
import com.taki.wms.domain.dto.ScheduleDeliveryResult;
import com.taki.wms.domain.entity.DeliverOrderDO;
import com.taki.wms.domain.entity.DeliveryOrderItemDO;
import com.taki.wms.domain.request.PickGoodsRequest;
import com.taki.wms.exception.WmsBizException;
import com.taki.wms.exception.WmsErrorCodeEnum;
import com.taki.wms.service.WmsService;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleState;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * @ClassName WmsApiService
 * @Description WMS 服务
 * @Author Long
 * @Date 2022/5/16 18:01
 * @Version 1.0
 */
@DubboService(version = "1.0.0",interfaceClass = WmsApi.class,retries = 0)
@Slf4j
public class WmsApiServiceImpl implements WmsApi {


    @Autowired
    private WmsService wmsService;



    @Override
    public ResponseData<PickDTO> pickGoods(PickGoodsRequest request) {
        try{

            return ResponseData.success(wmsService.pickGoods(request)) ;

        }catch (WmsBizException e){
          log.error("biz error",e.getErrorMessage());

          return ResponseData.error(e.getErrorCode(),e.getErrorMessage());

        }catch (Exception e){

            log.error("system error",e.getMessage());
            return ResponseData.error(e.getMessage());
        }

    }
    

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseData<Boolean> cancelPickGoods(String orderId) {
        try{
            return ResponseData.success(wmsService.cancelPickGoods(orderId)) ;

        }catch (WmsBizException e){
            log.error("biz error",e.getErrorMessage());

            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());

        }catch (Exception e){

            log.error("system error",e.getMessage());
            return ResponseData.error(e.getMessage());
        }
    }
}
