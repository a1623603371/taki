package com.taki.fulfill.builder;

import com.taki.common.utli.ObjectUtil;
import com.taki.fulfill.domain.entity.OrderFulfillDO;
import com.taki.fulfill.domain.entity.OrderFulfillItemDO;
import com.taki.fulfill.domain.request.ReceiveFulFillRequest;
import lombok.Data;

import java.util.List;

/**
 * @ClassName FulfillDataBuilder
 * @Description 订单履约构造器
 * @Author Long
 * @Date 2022/5/15 19:28
 * @Version 1.0
 */
@Data
public class FulfillDataBuilder {

    /**
     * 订单履约
     */
    private OrderFulfillDO orderFulfill;
    /**
     * 订单履约条目
     */
    private List<OrderFulfillItemDO> orderFulFillItems;

    /**
     * 接受订单履约请求
     */
    private ReceiveFulFillRequest receiveFulFillRequest;


    public FulfillDataBuilder(ReceiveFulFillRequest request){
        this.receiveFulFillRequest = request;
    }

    public  static  FulfillDataBuilder builder(ReceiveFulFillRequest receiveFulFillRequest){

        return new FulfillDataBuilder(receiveFulFillRequest);

    }

    public  FulfillDataBuilder builderOrderFulfill(String  fulfillId){

        orderFulfill = receiveFulFillRequest.clone(OrderFulfillDO.class);

        orderFulfill.setFulfillId(fulfillId);

        return this;

    }

    public FulfillDataBuilder buildOrderFulfillItem() {
        orderFulFillItems = ObjectUtil
                .convertList(receiveFulFillRequest.getReceiveOrderItems(), OrderFulfillItemDO.class);

        //设置履约单ID
        for(OrderFulfillItemDO item : orderFulFillItems) {
            item.setFulfillId(orderFulfill.getFulfillId());
        }

        return this;
    }

}
