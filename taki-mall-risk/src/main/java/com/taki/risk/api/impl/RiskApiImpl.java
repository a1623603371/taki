package com.taki.risk.api.impl;

import com.taki.common.utli.ResponseData;
import com.taki.risk.api.RiskApi;
import com.taki.risk.domain.dto.CheckOrderRiskDTO;
import com.taki.risk.domain.request.CheckOrderRiskRequest;
import org.apache.dubbo.config.annotation.DubboService;

/**
 * @ClassName RiskApiImpl
 * @Description 风控 API
 * @Author Long
 * @Date 2022/2/17 15:00
 * @Version 1.0
 */
@DubboService(version = "1.0.0",interfaceClass = RiskApi.class)
public class RiskApiImpl implements RiskApi {
    
    /** 
     * @description: 订单风控检查
     * @param checkOrderRiskRequest 订单风控检查参数
     * @return  订单风控检查结果
     * @author Long
     * @date: 2022/2/17 15:01
     */ 
    @Override
    public ResponseData<CheckOrderRiskDTO> checkOrderRisk(CheckOrderRiskRequest checkOrderRiskRequest) {
        //执行风控检查 TODO
        CheckOrderRiskDTO checkOrderRiskDTO = new CheckOrderRiskDTO();
        checkOrderRiskDTO.setResult(true);

        // 默认风控检查通过
        return ResponseData.success(checkOrderRiskDTO);
    }
}
