package com.taki.wms.api;

import com.taki.common.utlis.ResponseData;
import com.taki.wms.domain.dto.PickDto;
import com.taki.wms.domain.request.PickGoodsRequest;

/**
 * @ClassName WmsApi
 * @Description wms 仓储系统 api
 * @Author Long
 * @Date 2022/5/16 16:56
 * @Version 1.0
 */
public interface WmsApi {
    /**
     * @description: 拣货
     * @param request 拣货请求
     * @return
     * @author Long
     * @date: 2022/5/16 18:00
     */
    ResponseData<PickDto> pickGoods(PickGoodsRequest request);


    /** 
     * @description:  取消拣货
     * @param orderId 订单ID
     * @return  com.taki.common.utlis.ResponseData<java.lang.Boolean>
     * @author Long
     * @date: 2022/5/16 18:00
     */ 
    ResponseData<Boolean> cancelPickGoods(String orderId);
}
