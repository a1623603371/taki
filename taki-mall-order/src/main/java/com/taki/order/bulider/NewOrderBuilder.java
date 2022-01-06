package com.taki.order.bulider;

import com.taki.market.domain.dto.CalculateOrderAmountDTO;
import com.taki.market.request.CalculateOrderAmountRequest;
import com.taki.order.config.OrderProperties;
import com.taki.order.domin.entity.OrderInfoDO;
import com.taki.order.domin.request.CreateOrderRequest;
import com.taki.order.enums.OrderTypeEnum;
import com.taki.product.domian.dto.ProductSkuDTO;
import lombok.Data;

import java.util.List;

/**
 * @ClassName NewOrderBuilder
 * @Description 新订单构造器
 * @Author Long
 * @Date 2022/1/6 11:09
 * @Version 1.0
 */

public class NewOrderBuilder {

    /**
     * 创建订单请求
     */
    private CreateOrderRequest createOrderRequest;

    /**
     * 订单商品SKU 集合
     */
    private List<ProductSkuDTO> productSkuList;

    /**
     * 订单费用
     */
    private CalculateOrderAmountDTO calculateOrderAmount;

    /**
     * 订单配置
     */
    private OrderProperties orderProperties;

    /**
     * 全量订单数据
     */
    private FullOrderData fullOrderData;


    public NewOrderBuilder(CreateOrderRequest createOrderRequest, List<ProductSkuDTO> productSkuList, CalculateOrderAmountDTO calculateOrderAmount, OrderProperties orderProperties) {
        this.createOrderRequest = createOrderRequest;
        this.productSkuList = productSkuList;
        this.calculateOrderAmount = calculateOrderAmount;
        this.orderProperties = orderProperties;
        this.fullOrderData = new FullOrderData();
    }


    public  NewOrderBuilder builder (){
        OrderInfoDO orderInfo = new OrderInfoDO();
        orderInfo.setBusinessIdentifier(createOrderRequest.getBusinessIdentifier());
        orderInfo.setOrderId(createOrderRequest.getOrderId());
        orderInfo.setParentOrderId(null);
        orderInfo.setBusinessOrderId(null);
        orderInfo.setOrderType(OrderTypeEnum.NORMAL.getCode());
     //   orderInfo.setOrderStatus();

        return this;


    }
}
