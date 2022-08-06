package com.taki.customer.api;

import com.taki.common.utli.ResponseData;
import com.taki.customer.domain.request.CustomReviewReturnGoodsRequest;

/**
 * @ClassName CustomerApi
 * @Description 客服 对外  API
 * @Author Long
 * @Date 2022/4/4 16:22
 * @Version 1.0
 */
public interface CustomerApi {


    /**
     * @description: 客服 接受审核售后申请
     * @param request
     * @return  com.taki.common.utlis.ResponseData<java.lang.Boolean>
     * @author Long
     * @date: 2022/4/4 16:23
     */
    ResponseData<Boolean> customerAudit(CustomReviewReturnGoodsRequest request);
}
