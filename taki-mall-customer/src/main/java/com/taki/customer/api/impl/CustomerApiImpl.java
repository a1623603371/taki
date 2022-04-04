package com.taki.customer.api.impl;

import com.taki.common.utlis.ResponseData;
import com.taki.customer.api.CustomerApi;
import com.taki.customer.domain.request.CustomReviewReturnGoodsRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @ClassName CustomerApiImpl
 * @Description TODO
 * @Author Long
 * @Date 2022/4/4 17:32
 * @Version 1.0
 */
@DubboService
@Slf4j
public class CustomerApiImpl implements CustomerApi {
    @Override
    public ResponseData<Boolean> customerAudit(CustomReviewReturnGoodsRequest request) {
        log.info("收到提交审核");
        return ResponseData.success(true);
    }
}
