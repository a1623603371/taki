package com.taki.risk.api;

import com.taki.common.utlis.ResponseData;
import com.taki.risk.domain.dto.CheckOrderRiskDTO;
import com.taki.risk.domain.request.CheckOrderRiskRequest;

/**
 * @ClassName RiskApi
 * @Description 风控 API
 * @Author Long
 * @Date 2022/1/4 10:00
 * @Version 1.0
 */
public interface RiskApi {

    /**
     * @description: 订单风控检查
     * @param checkOrderRiskRequest 订单风控检查请求
     * @return 风控检查结果
     * @author Long
     * @date: 2022/1/4 10:01
     */
    ResponseData<CheckOrderRiskDTO> checkOrderRisk(CheckOrderRiskRequest checkOrderRiskRequest);
}
