package com.taki.order.controller;

import com.taki.common.page.PagingInfo;
import com.taki.common.utli.ResponseData;
import com.taki.order.api.AfterSaleApi;
import com.taki.order.api.AfterSaleQueryApi;
import com.taki.order.domain.dto.AfterSaleOrderDetailDTO;
import com.taki.order.domain.dto.AfterSaleOrderListDTO;
import com.taki.order.domain.dto.LackDTO;
import com.taki.order.domain.query.AfterSaleQuery;
import com.taki.order.domain.request.CancelOrderRequest;
import com.taki.order.domain.request.LackRequest;
import com.taki.order.domain.request.ReturnGoodsOrderRequest;
import com.taki.order.domain.request.RevokeAfterSaleRequest;
import com.taki.order.service.OrderAfterSaleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @ClassName AfterSaleController
 * @Description 订单售后流程
 * @Author Long
 * @Date 2022/3/5 15:01
 * @Version 1.0
 */
@Api(value = "AfterSaleController",tags = "订单售后流程")
@RestController
@RequestMapping("/afterSale")
public class AfterSaleController {

    @Autowired
    private OrderAfterSaleService orderAfterSaleService;


    @DubboReference
    private AfterSaleApi afterSaleApi;


    @DubboReference
    private AfterSaleQueryApi afterSaleQueryApi;
    /**
     * @description: 取消订单
     * @param cancelOrderRequest 取消订单请求参数
     * @return  处理结果
     * @author Long
     * @date: 2022/4/3 15:22
     */
    @ApiOperation("取消订单")
    @PostMapping("/cancelOrder")
    public ResponseData<Boolean> cancelOrder(@RequestBody CancelOrderRequest cancelOrderRequest){
        return ResponseData.success(orderAfterSaleService.cancelOrder(cancelOrderRequest));
    }

    /**
     * @description: 用户退货售后
     * @param request 用户申请售后参数请求
     * @return 处理结果
     * @author Long
     * @date: 2022/4/3 15:25
     */
    @ApiOperation("用户退货售后")
    @PostMapping("/applyAfterSale")
    public ResponseData<Boolean> applyAfterSale( @RequestBody ReturnGoodsOrderRequest request) {
        return ResponseData.success(orderAfterSaleService.processApplyAfterSale(request));
    }

    /** 
     * @description: 缺品请求
     * @param lockRequest 缺品请求
     * @return  缺品数据
     * @author Long
     * @date: 2022/4/4 17:41
     */ 
    @ApiOperation("缺品请求")
    @PostMapping("/lockItem")
    public ResponseData<LackDTO>lockItem(@RequestBody LackRequest lockRequest){

        return afterSaleApi.lockItem(lockRequest);
    }
    
    /** 
     * @description: 用户撤销销售申请
     * @param revokeAfterSaleRequest 用户撤销销售申请请求
     * @return
     * @author Long
     * @date: 2022/4/4 17:51
     */ 
    @ApiOperation("用户撤销售后申请")
    @PostMapping("/revokeAfterSale")
    public  ResponseData<Boolean> revokeAfterSale(@RequestBody RevokeAfterSaleRequest revokeAfterSaleRequest){
        return afterSaleApi.revokeAfterSale(revokeAfterSaleRequest);
    }

    /** 
     * @description: 查询售后列表
     * @param 
     * @return
     * @author Long
     * @date: 2022/4/4 17:52
     */ 
    @ApiOperation("查询售后列表")
    @PostMapping("/listAfterSales")
    public ResponseData<PagingInfo<AfterSaleOrderListDTO>> listAfterSales(@RequestBody AfterSaleQuery query){

        return afterSaleQueryApi.listAfterSale(query);

    }

    /**
     * @description: 查询售后信息详情
     * @param afterSaleId 售后Id
     * @return
     * @author Long
     * @date: 2022/4/4 17:52
     */
    @ApiOperation("查询售后信息详情")
    @PostMapping("/afterSaleDetail")
    public  ResponseData<AfterSaleOrderDetailDTO> afterSaleDetail(@RequestParam("afterSaleId") Long afterSaleId){

        return afterSaleQueryApi.afterSaleDetail(afterSaleId);
    }
}
