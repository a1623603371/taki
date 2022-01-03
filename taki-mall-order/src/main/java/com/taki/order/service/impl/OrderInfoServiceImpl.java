package com.taki.order.service.impl;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import com.taki.common.enums.AmountTypeEnum;
import com.taki.common.enums.PayTypeEnum;
import com.taki.common.exception.ServiceException;
import com.taki.common.utlis.ParamCheckUtil;
import com.taki.order.dao.OrderAutoNoDao;
import com.taki.order.domin.dto.CreateOrderDTO;
import com.taki.order.domin.dto.GenOrderIdDTO;
import com.taki.order.domin.request.CreateOrderRequest;
import com.taki.order.domin.request.GenOrderIdRequest;
import com.taki.order.enums.*;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.manager.OrderNoManager;
import com.taki.order.mapper.OrderAutoNoMapper;
import com.taki.order.service.OrderInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName OrderInfoServiceImpl
 * @Description 订单 service接口实现
 * @Author Long
 * @Date 2022/1/2 17:07
 * @Version 1.0
 */
@Service
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private OrderNoManager orderNoManager;

    @Override
    public GenOrderIdDTO getGenOrderIdDTO(GenOrderIdRequest genOrderIdRequest) {

        String userId = genOrderIdRequest.getUserId();
        ParamCheckUtil.checkStringNonEmpty(userId);

        Integer businessIdentifier = genOrderIdRequest.getBusinessIdentifier();
        ParamCheckUtil.checkObjectNonNull(businessIdentifier);
        String orderId = orderNoManager.genOrderId(OrderAutoTypeEnum.SALE_ORDER.getCode(),userId);
        GenOrderIdDTO genOrderId = new GenOrderIdDTO();
        genOrderId.setOrderId(orderId);
        return genOrderId;
    }

    @Override
    public CreateOrderDTO createOrder(CreateOrderRequest createOrderRequest) {

        // 入参检查
        checkCreateOrderRequestParam(createOrderRequest);



        return null;
    }
    /**
     * @description: 检查创建订单请求参数
     * @param createOrderRequest 创建订单请求
     * @return  void
     * @author Long
     * @date: 2022/1/3 14:58
     */
    private void checkCreateOrderRequestParam(CreateOrderRequest createOrderRequest) {
        ParamCheckUtil.checkObjectNonNull(createOrderRequest);

        // 订单Id
        String orderId = createOrderRequest.getOrderId();
        ParamCheckUtil.checkStringNonEmpty(orderId);

        //业务线标识
        Integer businessIdentifier = createOrderRequest.getBusinessIdentifier();
        ParamCheckUtil.checkObjectNonNull(businessIdentifier, OrderErrorCodeEnum.BUSINESS_IDENTIFIER_ERROR);

        if (BusinessIdentifierEnum.getByCode(businessIdentifier) == null){
            throw new ServiceException(OrderErrorCodeEnum.BUSINESS_IDENTIFIER_ERROR);
        }
        // 用户Id
        String userId = createOrderRequest.getUserId();
        ParamCheckUtil.checkStringNonEmpty(userId);

        //订单类型

        Integer orderType = createOrderRequest.getOrderType();;
        ParamCheckUtil.checkObjectNonNull(orderType,OrderErrorCodeEnum.ORDER_TYPE_IS_NULL);

        if (OrderTypeEnum.getByCode(orderType) == null){
            throw  new ServiceException(OrderErrorCodeEnum.ORDER_TYPE_ERROR);
        }

        //卖家Id
        String sellerId = createOrderRequest.getSellerId();
        ParamCheckUtil.checkStringNonEmpty(sellerId);

        //配送类型
        Integer deliverType = createOrderRequest.getDeliveryType();
        ParamCheckUtil.checkObjectNonNull(deliverType,OrderErrorCodeEnum.USER_ADDRESS_ERROR);
        if (DeliveryTypeEnum.getByCode(deliverType) == null){
            throw new ServiceException(OrderErrorCodeEnum.DELIVERY_TYPE_ERROR);
        }

        //地址信息
        String province = createOrderRequest.getProvince();
        String city = createOrderRequest.getCity();
        String area = createOrderRequest.getArea();
        String street = createOrderRequest.getStreet();

        ParamCheckUtil.checkStringNonEmpty(province,OrderErrorCodeEnum.USER_ADDRESS_ERROR);
        ParamCheckUtil.checkStringNonEmpty(city,OrderErrorCodeEnum.USER_ADDRESS_ERROR);
        ParamCheckUtil.checkStringNonEmpty(area,OrderErrorCodeEnum.USER_ADDRESS_ERROR);
        ParamCheckUtil.checkStringNonEmpty(street,OrderErrorCodeEnum.USER_ADDRESS_ERROR);

        // 区域Id
        String regionId = createOrderRequest.getRegionId();
        ParamCheckUtil.checkStringNonEmpty(regionId,OrderErrorCodeEnum.REGION_ID_IS_NULL);

        // 经纬度
        BigDecimal lon = createOrderRequest.getLon();
        BigDecimal lat = createOrderRequest.getLat();
        ParamCheckUtil.checkObjectNonNull(lon,OrderErrorCodeEnum.USER_LOCATION_IS_NULL);
        ParamCheckUtil.checkObjectNonNull(lat,OrderErrorCodeEnum.USER_LOCATION_IS_NULL);

        // 收货人信息

        String receiverName = createOrderRequest.getReceiverName();
        String receiverPhone = createOrderRequest.getReceiverPhone();
        ParamCheckUtil.checkStringNonEmpty(receiverName,OrderErrorCodeEnum.ORDER_RECEIVER_IS_NULL);
        ParamCheckUtil.checkStringNonEmpty(receiverPhone,OrderErrorCodeEnum.ORDER_RECEIVER_PHONE_IS_NULL);

        // 客户端设备信息
        String clientIp = createOrderRequest.getClientIp();
        ParamCheckUtil.checkStringNonEmpty(clientIp,OrderErrorCodeEnum.CLIENT_IP_IS_NULL);

        //商品条目信息
        List<CreateOrderRequest.OrderItemRequest> orderItemRequests = createOrderRequest.getOrderItemRequests();
        ParamCheckUtil.checkObjectNonNull(orderItemRequests,OrderErrorCodeEnum.ORDER_ITEM_IS_NULL);
        orderItemRequests.forEach(orderItemRequest -> {
            Integer productType = orderItemRequest.getProductType();
            Integer saleQuantity = orderItemRequest.getSaleQuantity();
            String  skuCode = orderItemRequest.getSkuCode();
            ParamCheckUtil.checkObjectNonNull(productType,OrderErrorCodeEnum.ORDER_ITEM_PARAM_ERROR);
            ParamCheckUtil.checkObjectNonNull(saleQuantity,OrderErrorCodeEnum.ORDER_ITEM_PARAM_ERROR);
            ParamCheckUtil.checkStringNonEmpty(skuCode,OrderErrorCodeEnum.ORDER_ITEM_PARAM_ERROR);
        });

        // 订单信息费用

        List<CreateOrderRequest.OrderAmountRequest> orderAmountRequests = createOrderRequest.getOrderAmountRequests();
        ParamCheckUtil.checkObjectNonNull(orderAmountRequests,OrderErrorCodeEnum.ORDER_AMOUNT_IS_NULL);
        orderAmountRequests.forEach(orderAmountRequest -> {
            Integer amountType = orderAmountRequest.getAmountType();
            ParamCheckUtil.checkObjectNonNull(amountType,OrderErrorCodeEnum.ORDER_AMOUNT_TYPE_PARAM_ERROR);
            if (AmountTypeEnum.getByCode(amountType) == null){
                throw  new ServiceException(OrderErrorCodeEnum.ORDER_AMOUNT_TYPE_PARAM_ERROR);
            }
        });

        Map<Integer,Integer> orderAmountMap = orderAmountRequests.stream().collect(Collectors.toMap(CreateOrderRequest.OrderAmountRequest::getAmountType, CreateOrderRequest.OrderAmountRequest::getAmount));

        // 订单原价不能为空
        if (orderAmountMap.get(AmountTypeEnum.ORIGIN_PAY_AMOUNT) == null){
            throw new ServiceException(OrderErrorCodeEnum.ORDER_AMOUNT_TYPE_PARAM_ERROR);
        }
        // 订单运费不能为空
        if (orderAmountMap.get(AmountTypeEnum.SHIPPING_AMOUNT) == null){
            throw new ServiceException(OrderErrorCodeEnum.ORDER_SHIPPING_AMOUNT_IS_NULL);
        }
        // 订单实付价格不能为空
        if (orderAmountMap.get(AmountTypeEnum.REAL_PAY_AMOUNT) == null){
            throw new ServiceException(OrderErrorCodeEnum.ORDER_REAL_PAY_AMOUNT_IS_NULL);
        }

        if (!StringUtils.isNotBlank(createOrderRequest.getCouponId())){
            if(orderAmountMap.get(AmountTypeEnum.COUPON_DISCOUNT_AMOUNT) == null){
                throw new ServiceException(OrderErrorCodeEnum.ORDER_DISCOUNT_AMOUNT_IS_NULL);
            }

        }

        // 订单支付信息

        List<CreateOrderRequest.PaymentRequest> paymentRequests = createOrderRequest.getPaymentRequests();
        ParamCheckUtil.checkObjectNonNull(paymentRequests,OrderErrorCodeEnum.ORDER_PAYMENT_IS_NULL);
        paymentRequests.forEach(paymentRequest -> {
            Integer payType = paymentRequest.getPayType();
            Integer accountType = paymentRequest.getAccountType();

            if (payType == null || PayTypeEnum.getByCode(payType) == null){
                throw  new ServiceException(OrderErrorCodeEnum.PAY_TYPE_PARAM_ERROR);
            }

            if ( accountType == null || AccountTypeEnum.getByCode(accountType) == null){
                throw  new ServiceException(OrderErrorCodeEnum.ACCOUNT_TYPE_PARAM_ERROR);
            }

        });




    }
}
