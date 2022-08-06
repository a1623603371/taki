package com.taki.order.remote;

import com.taki.common.utli.ResponseData;
import com.taki.order.exception.OrderBizException;
import com.taki.risk.api.RiskApi;
import com.taki.risk.domain.dto.CheckOrderRiskDTO;
import com.taki.risk.domain.request.CheckOrderRiskRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @ClassName RiskRemote
 * @Description 风控 系统远程 api
 * @Author Long
 * @Date 2022/6/8 15:43
 * @Version 1.0
 */
@Component
public class RiskRemote {


    @DubboReference(version = "1.0.0",retries = 0)
    private RiskApi riskApi;

    /** 
     * @description:  订单风控检查
     * @param 
     * @return
     * @author Long
     * @date: 2022/6/8 15:49
     */ 
    public CheckOrderRiskDTO checkOrderRisk(CheckOrderRiskRequest checkOrderRiskRequest){

        ResponseData<CheckOrderRiskDTO> result = riskApi.checkOrderRisk(checkOrderRiskRequest);

        if (!result.getSuccess()){
            throw new OrderBizException(result.getCode(),result.getMessage());
        }
        return result.getData();
    }
}
