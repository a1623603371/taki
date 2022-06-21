package com.taki.fulfill.remote;

import com.taki.common.utlis.ResponseData;
import com.taki.fulfill.exection.FulfillBizException;
import com.taki.wms.api.WmsApi;
import com.taki.wms.domain.dto.PickDTO;
import com.taki.wms.domain.request.PickGoodsRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName WmsRemote
 * @Description wms 服务 接口
 * @Author Long
 * @Date 2022/6/21 12:29
 * @Version 1.0
 */
@Component
public class WmsRemote {


    @DubboReference(version = "1.0.0",retries = 0)
    private WmsApi wmsApi;


    /***
     * 拣货
     * @param pickGoodsRequest 拣货商品 请求
     * @return
     */
    public PickDTO pickGoods(PickGoodsRequest pickGoodsRequest){

        ResponseData<PickDTO>  result = wmsApi.pickGoods(pickGoodsRequest);

        if (!result.getSuccess()){
            throw new FulfillBizException(result.getCode(),result.getMessage());
        }

        return result.getData();

    }

    /**
     * 取消拣货
     * @param orderId 订单Id
     * @return
     */
    public Boolean cancelPickGoods(String orderId){
      ResponseData<Boolean> result = wmsApi.cancelPickGoods(orderId);

      if (!result.getSuccess()){
          throw new FulfillBizException(result.getCode(),result.getMessage());
      }

      return result.getData();
    }

}
