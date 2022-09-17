package com.taki.order.remote;

import com.taki.common.utli.ResponseData;
import com.taki.customer.api.CustomerApi;
import com.taki.customer.domain.request.CustomReviewReturnGoodsRequest;
import com.taki.order.exception.OrderBizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName CustomerRemote
 * @Description TODO
 * @Author Long
 * @Date 2022/9/14 23:07
 * @Version 1.0
 */
@Slf4j
@Component
public class CustomerRemote {


    @Autowired
    private CustomerApi customerApi;
    /**
     * @description: 客服 接受审核售后申请
     * @param request
     * @return  com.taki.common.utlis.ResponseData<java.lang.Boolean>
     * @author Long
     * @date: 2022/4/4 16:23
     */
  Boolean customerAudit(CustomReviewReturnGoodsRequest request){

      ResponseData<Boolean> responseData = customerApi.customerAudit(request);

      if (!responseData.getSuccess()){
          throw new OrderBizException(responseData.getCode(),responseData.getMessage());
      }
      return responseData.getData();

  }

}
