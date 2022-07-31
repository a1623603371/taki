package com.taki.wms.service;

import com.taki.common.utlis.ResponseData;
import com.taki.wms.domain.dto.PickDTO;
import com.taki.wms.domain.request.PickGoodsRequest;

/**
 * @ClassName WmsService
 * @Description WMS service 组件
 * @Author Long
 * @Date 2022/7/31 21:33
 * @Version 1.0
 */
public interface WmsService {

    /**
     * @description: 拣货
     * @param request 拣货 请求
     * @return
     * @author Long
     * @date: 2022/5/17 14:12
     */
    PickDTO pickGoods(PickGoodsRequest request);

    /** 
     * @description: 取消拣货
     * @param orderId
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/7/31 22:03
     */ 
    Boolean cancelPickGoods(String orderId);
}
