package com.taki.order.service;

import com.taki.common.page.PagingInfo;
import com.taki.order.domain.dto.AfterSaleOrderDetailDTO;
import com.taki.order.domain.dto.AfterSaleOrderListDTO;
import com.taki.order.domain.dto.OrderLackItemDTO;
import com.taki.order.domain.query.AfterSaleQuery;

import java.util.List;

/**
 * @ClassName AfterSaleQueryService
 * @Description 售后查询 service
 * @Author Long
 * @Date 2022/3/3 22:13
 * @Version 1.0
 */
public interface AfterSaleQueryService {

    /**
     * @description: 校验列表查询参数
     * @param afterSaleQuery 售后查询条件
     * @return  void
     * @author Long
     * @date: 2022/3/3 22:23
     */
    void checkQueryParam(AfterSaleQuery afterSaleQuery);

    /** 
     * @description: 执行查询条件
     * @param query 查询条件
     * @return  售后订单集合
     * @author Long
     * @date: 2022/3/3 22:25
     */ 
    PagingInfo<AfterSaleOrderListDTO> executeListQuery(AfterSaleQuery query);

    /*** 
     * @description:  查询售后数据详情
     * @param afterSaleId 售后Id
     * @return 售后订单详情
     * @author Long
     * @date: 2022/3/3 22:28
     */ 
    AfterSaleOrderDetailDTO afterSaleDetail(String afterSaleId);
    
    /** 
     * @description:  查询缺品信息
     * @param orderId 订单Id
     * @return  缺品信息集合
     * @author Long
     * @date: 2022/3/3 22:34
     */ 
    List<OrderLackItemDTO> getOrderLackItemInfo(String orderId);
}
