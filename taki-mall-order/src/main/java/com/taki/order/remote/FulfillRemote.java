package com.taki.order.remote;

import com.taki.common.utli.ResponseData;
import com.taki.fulfill.api.FulFillApi;
import com.taki.fulfill.domain.request.CancelFulfillRequest;
import com.taki.order.exception.OrderBizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName FulfillRemote
 * @Description TODO
 * @Author Long
 * @Date 2022/9/14 23:10
 * @Version 1.0
 */
@Component
@Slf4j
public class FulfillRemote {


    @Autowired
    private FulFillApi fulFillApi;


    /**
     * @description:  履约通知停止配送
     * @param cancelFulfillRequest 履约通知停止配送 请求
     * @return
     * @author Long
     * @date: 2022/3/4 11:57
     */
    public  Boolean cancelFulfill(CancelFulfillRequest cancelFulfillRequest){
        ResponseData<Boolean> responseData = fulFillApi.cancelFulfill(cancelFulfillRequest);
        if (!responseData.getSuccess()){
            throw new OrderBizException(responseData.getCode(),responseData.getMessage());
        }
        return responseData.getData();

    }
}
