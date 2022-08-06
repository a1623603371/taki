package com.taki.tms.api.impl;

import com.taki.common.utli.ResponseData;
import com.taki.tms.api.TmsApi;
import com.taki.tms.domain.dto.SendOutDTO;
import com.taki.tms.domain.request.SendOutRequest;
import com.taki.tms.exception.TmsBizException;
import com.taki.tms.service.LogisticOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName TmsApiImpl
 * @Description TODO
 * @Author Long
 * @Date 2022/5/17 15:09
 * @Version 1.0
 */
@DubboService(version = "1.0.0" , interfaceClass = TmsApi.class,retries = 0)
@Slf4j
public class TmsApiImpl implements TmsApi {


    @Autowired
    private LogisticOrderService logisticOrderService;


    @Override
    public ResponseData<SendOutDTO> sendOut(SendOutRequest request) {

        try {
            SendOutDTO sendOutDTO =  logisticOrderService.sendOut(request);
            return ResponseData.success(sendOutDTO);
        } catch (TmsBizException e){
            log.error("biz error,e={}",e.getMessage());

            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());
        } catch (Exception e){
            log.error("system error,e={}",e.getMessage());
          return   ResponseData.error(e.getMessage());
        }


    }

    @Override
    public ResponseData<Boolean> cancelSendOut(String orderId) {
        try {
            Boolean result =  logisticOrderService.cancelSendOut(orderId);
            return ResponseData.success(result);
        } catch (TmsBizException e){
            log.error("biz error,e={}",e.getMessage());

            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());
        } catch (Exception e){
            log.error("system error,e={}",e.getMessage());
            return   ResponseData.error(e.getMessage());
        }

    }
}
