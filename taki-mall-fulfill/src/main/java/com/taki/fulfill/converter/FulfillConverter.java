package com.taki.fulfill.converter;

import com.taki.fulfill.domain.entity.OrderFulfillDO;
import com.taki.fulfill.domain.entity.OrderFulfillItemDO;
import com.taki.fulfill.domain.request.ReceiveFulfillRequest;
import com.taki.fulfill.domain.request.ReceiveOrderItemRequest;
import com.taki.tms.domain.request.SendOutRequest;
import com.taki.wms.domain.request.PickGoodsRequest;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @ClassName FulfillConverter
 * @Description TODO
 * @Author Long
 * @Date 2022/9/17 19:47
 * @Version 1.0
 */
@Mapper(componentModel = "spring")
public interface FulfillConverter {

    /**
     * 转换对象
     *
     * @param request 对象
     * @return 对象
     */
    OrderFulfillDO convertFulFillRequest(ReceiveFulfillRequest request);

    /**
     * 转换对象
     *
     * @param receiveOrderItem 对象
     * @return 对象
     */
    OrderFulfillItemDO convertFulFillRequest(ReceiveOrderItemRequest receiveOrderItem);

    /**
     * 转换对象
     *
     * @param receiveOrderItems 对象
     * @return 对象
     */
    List<OrderFulfillItemDO> convertFulFillRequest(List<ReceiveOrderItemRequest> receiveOrderItems);

    /**
     * 转换对象
     *
     * @param fulfillRequest 对象
     * @return 对象
     */
    SendOutRequest convertReceiveFulfillRequest(ReceiveFulfillRequest fulfillRequest);

    /**
     * 转换对象
     *
     * @param receiveOrderItem 对象
     * @return 对象
     */
    SendOutRequest.OrderItemRequest convertSendOutOrderItemRequest(ReceiveOrderItemRequest receiveOrderItem);

    /**
     * 转换对象
     *
     * @param receiveOrderItems 对象
     * @return 对象
     */
    List<SendOutRequest.OrderItemRequest> convertSendOutOrderItemRequest(List<ReceiveOrderItemRequest> receiveOrderItems);

    /**
     * 转换对象
     *
     * @param fulfillRequest 对象
     * @return 对象
     */
    PickGoodsRequest convertPickGoodsRequest(ReceiveFulfillRequest fulfillRequest);

    /**
     * 转换对象
     *
     * @param receiveOrderItem 对象
     * @return 对象
     */
    PickGoodsRequest.OrderItemRequest convertPickOrderItemRequest(ReceiveOrderItemRequest receiveOrderItem);

    /**
     * 转换对象
     *
     * @param receiveOrderItems 对象
     * @return 对象
     */
    List<PickGoodsRequest.OrderItemRequest> convertPickOrderItemRequest(List<ReceiveOrderItemRequest> receiveOrderItems);


}
