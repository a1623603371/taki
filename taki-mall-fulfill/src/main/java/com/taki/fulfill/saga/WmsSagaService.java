package com.taki.fulfill.saga;

import com.taki.fulfill.domain.request.ReceiveFulFillRequest;

/**
 * @ClassName WmsSagaService
 * @Description wms saga
 * @Author Long
 * @Date 2022/5/16 16:35
 * @Version 1.0
 */
public interface WmsSagaService {

    /**
     *
     * 拣货
     * @param request
     * @return
     */
    Boolean pickGoods(ReceiveFulFillRequest request);


    /**
     * 拣货补偿
     * @param request
     * @return
     */
    Boolean pickGoodsCompensate(ReceiveFulFillRequest request);
}
