package com.taki.fulfill.remote;

import com.taki.common.utli.ResponseData;
import com.taki.fulfill.exection.FulfillBizException;
import com.taki.tms.api.TmsApi;
import com.taki.tms.domain.dto.SendOutDTO;
import com.taki.tms.domain.request.SendOutRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @ClassName TmsRemote
 * @Description TODO
 * @Author Long
 * @Date 2022/6/21 12:23
 * @Version 1.0
 */
@Component
public class TmsRemote {

    @DubboReference(version = "1.0.0",retries = 0)
    private TmsApi tmsApi;


    /**
     * 发货
     * @return
     */
    public SendOutDTO sendOut (SendOutRequest request){

        ResponseData<SendOutDTO> result = tmsApi.sendOut(request);

        if (!result.getSuccess()){
            throw new FulfillBizException(result.getCode(),result.getMessage());
        }        return result.getData();
    }

    /**
     * 取消发货
     * @param orderId 订单Id
     */
    public  Boolean  cancelSendOut(String orderId){
        ResponseData<Boolean> result = tmsApi.cancelSendOut(orderId);

        if (!result.getSuccess()){
            throw new FulfillBizException(result.getCode(),result.getMessage());
        }

        return result.getData();

    }
}
