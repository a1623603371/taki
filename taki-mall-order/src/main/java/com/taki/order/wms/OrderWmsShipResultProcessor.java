package com.taki.order.wms;

import com.taki.order.domain.dto.WmsShipDTO;

/**
 * @ClassName OrderWmsShipResultProcessor
 * @Description 订单配送结果处理器
 * @Author Long
 * @Date 2022/4/6 14:56
 * @Version 1.0
 */
public interface OrderWmsShipResultProcessor {


    /** 
     * @description:  执行具体的业务逻辑
     * @param wmsShipDTO
     * @return  void
     * @author Long
     * @date: 2022/4/6 15:07
     */ 
    void execute(WmsShipDTO wmsShipDTO);
}
