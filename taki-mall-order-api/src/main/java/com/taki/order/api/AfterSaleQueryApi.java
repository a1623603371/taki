package com.taki.order.api;

import com.taki.common.page.PagingInfo;
import com.taki.common.utli.ResponseData;
import com.taki.order.domain.dto.AfterSaleOrderDetailDTO;
import com.taki.order.domain.dto.AfterSaleOrderListDTO;
import com.taki.order.domain.query.AfterSaleQuery;

/**
 * @ClassName AfterSaleQueryApi
 * @Description 售后查询 对外 查询接口
 * @Author Long
 * @Date 2022/4/4 17:54
 * @Version 1.0
 */
public interface AfterSaleQueryApi {

    /** 
     * @description: 查询 售后列表
     * @param query 查询条件
     * @return
     * @author Long
     * @date: 2022/4/4 17:55
     */ 
    ResponseData<PagingInfo<AfterSaleOrderListDTO>> listAfterSale( AfterSaleQuery query);

    /**
     * @description: 根据售后Id 查询 售后订单信息
     * @param afterSaleId 售后Id
     * @return
     * @author Long
     * @date: 2022/4/4 17:56
     */
    ResponseData<AfterSaleOrderDetailDTO> afterSaleDetail(Long  afterSaleId);
}
