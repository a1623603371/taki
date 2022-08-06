package com.taki.order.remote;

import com.taki.common.utli.ResponseData;
import com.taki.market.api.MarketApi;
import com.taki.market.domain.dto.CalculateOrderAmountDTO;
import com.taki.market.request.CalculateOrderAmountRequest;
import com.taki.order.exception.OrderBizException;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @ClassName marketRemote
 * @Description 营销系统远程api
 * @Author Long
 * @Date 2022/6/8 15:13
 * @Version 1.0
 */
@Component
@Slf4j
public class MarketRemote {

    @DubboReference(version = "1.0.0",retries = 0)
    private MarketApi marketApi;


    /**
     * @description: 调用 计算 订单 费用接口
     * @param calculateOrderAmountRequest 计算 订单 费用 请求 参数
     * @return
     * @author Long
     * @date: 2022/6/8 15:15
     */
    public CalculateOrderAmountDTO calculateOrderAmount(CalculateOrderAmountRequest calculateOrderAmountRequest){

        ResponseData<CalculateOrderAmountDTO> responseResult = marketApi.calculateOrderAmount(calculateOrderAmountRequest);

        if (!responseResult.getSuccess()){
            log.error("调用营销服务计算价格失败错误信息：{}",responseResult.getMessage());
            throw new OrderBizException(responseResult.getCode(),responseResult.getMessage());
        }
        return responseResult.getData();
    }
}
