package com.taki.order.service.amount;

import com.taki.order.dao.OrderItemDao;
import com.taki.order.domain.dto.AfterSaleItemDTO;
import com.taki.order.domain.dto.OrderItemDTO;
import com.taki.order.domain.entity.AfterSaleItemDO;
import com.taki.order.domain.entity.OrderItemDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName AfterSaleAmountService
 * @Description 售后金额 service 组件
 * @Author Long
 * @Date 2022/3/11 11:57
 * @Version 1.0
 */
@Component
public class AfterSaleAmountService {

    @Autowired
    private OrderItemDao orderItemDao;



    /** 
     * @description:  计算订单条目缺品实际退款金额
     * @param orderItem 订单条目
     * @param lackNum 缺品数量
     * @return  实际退款金额
     * @author Long
     * @date: 2022/3/11 14:14
     */ 
    public BigDecimal calculateOrderItemLackRealRefundAmount(OrderItemDO orderItem, Integer lackNum){
        BigDecimal rate = new BigDecimal(lackNum).divide(new BigDecimal(orderItem.getSaleQuantity()),2,BigDecimal.ROUND_HALF_UP);
        BigDecimal itemRefundAmount = orderItem.getPayAmount().multiply(rate);
        return itemRefundAmount;
    }

    /** 
     * @description: 计算订单缺品申请退款金额
     * @param lackItems  售后条目
     * @return  申请退款金额
     * @author Long
     * @date: 2022/3/11 14:26
     */ 
    public  BigDecimal calculateOrderLackApplyRefundAmount(List<AfterSaleItemDO> lackItems){
        BigDecimal applyRefundAmount = BigDecimal.ZERO;

        for (AfterSaleItemDO lackItem : lackItems) {
            applyRefundAmount = applyRefundAmount.add(lackItem.getApplyRefundAmount());
        }
        return applyRefundAmount;
    }


    /** 
     * @description: 计算订单缺品总实际退款金额
     * @param lackItems 缺品条目
     * @return  实际退款金额
     * @author Long
     * @date: 2022/3/11 14:31
     */ 
    public BigDecimal calculateOrderLackRealRefundAmount (List<AfterSaleItemDO> lackItems){

        BigDecimal realRefundAmount = BigDecimal.ZERO;

        for (AfterSaleItemDO lackItem : lackItems) {

            realRefundAmount = realRefundAmount.add(lackItem.getRealRefundAmount());

        }

        return realRefundAmount;
    }

}
