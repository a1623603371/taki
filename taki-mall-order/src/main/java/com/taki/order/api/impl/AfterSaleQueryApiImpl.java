package com.taki.order.api.impl;

import com.taki.common.page.PagingInfo;
import com.taki.common.utlis.ParamCheckUtil;
import com.taki.common.utlis.ResponseData;
import com.taki.order.api.AfterSaleQueryApi;
import com.taki.order.domain.dto.AfterSaleOrderDetailDTO;
import com.taki.order.domain.dto.AfterSaleOrderListDTO;
import com.taki.order.domain.query.AfterSaleQuery;
import com.taki.order.exception.OrderBizException;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.service.AfterSaleQueryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName AfterSaleQueryApiImpl
 * @Description TODO
 * @Author Long
 * @Date 2022/4/4 17:57
 * @Version 1.0
 */
@Slf4j
@DubboService(version = "1.0.0",interfaceClass = AfterSaleQueryApi.class)
public class AfterSaleQueryApiImpl implements AfterSaleQueryApi {


    @Autowired
    private AfterSaleQueryService afterSaleQueryService;

    @Override
    public ResponseData<PagingInfo<AfterSaleOrderListDTO>> listAfterSale(AfterSaleQuery query) {
        try {
            // 1 参数效验
            afterSaleQueryService.checkQueryParam(query);
            //2.查询
            return ResponseData.success(afterSaleQueryService.executeListQuery(query));

        }catch (OrderBizException e){
            log.error("biz error",e);

            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());

        }catch (Exception e){
            log.error("system error",e);

            return ResponseData.error(e.getMessage());
        }

    }

    @Override
    public ResponseData<AfterSaleOrderDetailDTO> afterSaleDetail(Long afterSaleId) {
        try {
            //1.参数效验
            ParamCheckUtil.checkObjectNonNull(afterSaleId, OrderErrorCodeEnum.AFTER_SALE_ID_IS_NULL);

            return ResponseData.success(afterSaleQueryService.afterSaleDetail(String.valueOf(afterSaleId)));
        }catch (OrderBizException e){
            log.error("biz error",e);

            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());

        }catch (Exception e){
            log.error("system error",e);

            return ResponseData.error(e.getMessage());
        }

    }
}
