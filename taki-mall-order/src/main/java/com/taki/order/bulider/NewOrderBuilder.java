package com.taki.order.bulider;

import com.taki.common.enums.AmountTypeEnum;
import com.taki.common.enums.OrderOperateTypeEnum;
import com.taki.common.utlis.ObjectUtil;
import com.taki.market.domain.dto.CalculateOrderAmountDTO;
import com.taki.order.config.OrderProperties;
import com.taki.order.domian.dto.OrderAmountDTO;
import com.taki.product.domian.dto.OrderAmountDetailDTO;
import com.taki.order.domin.entity.*;
import com.taki.order.domian.request.CreateOrderRequest;
import com.taki.order.enums.*;
import com.taki.product.domian.dto.ProductSkuDTO;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    /** 
     * @description:  构建OrderInfoDo对象
     * @param 
     * @return  com.taki.order.bulider.NewOrderBuilder
     * @author Long
     * @date: 2022/1/7 9:43
     */ 
    public  NewOrderBuilder builder (){
        OrderInfoDO orderInfo = new OrderInfoDO();
        orderInfo.setBusinessIdentifier(createOrderRequest.getBusinessIdentifier());
        orderInfo.setOrderId(createOrderRequest.getOrderId());
        orderInfo.setParentOrderId(null);
        orderInfo.setBusinessOrderId(null);
        orderInfo.setOrderType(OrderTypeEnum.NORMAL.getCode());
        orderInfo.setOrderStatus(OrderStatusEnum.CREATED.getCode());
        orderInfo.setCannelType(null);
        orderInfo.setCannelTime(null);
        orderInfo.setSellerId(createOrderRequest.getSellerId());
        orderInfo.setUserId(createOrderRequest.getUserId());

        List<CreateOrderRequest.OrderAmountRequest> orderAmountRequests = createOrderRequest.getOrderAmountRequests();

        Map<Integer, BigDecimal>orderAmountMap = orderAmountRequests.stream().collect(
                Collectors.toMap(CreateOrderRequest.OrderAmountRequest::getAmountType, CreateOrderRequest.OrderAmountRequest::getAmount));

        orderInfo.setTotalAmount(orderAmountMap.get(AmountTypeEnum.ORIGIN_PAY_AMOUNT.getCode()));

        orderInfo.setPayAmount(orderAmountMap.get(AmountTypeEnum.REAL_PAY_AMOUNT.getCode()));

        List<CreateOrderRequest.PaymentRequest> paymentRequests = createOrderRequest.getPaymentRequests();

        if (paymentRequests != null && paymentRequests.isEmpty()){
        orderInfo.setPayType(paymentRequests.get(0).getPayType());
        }
        orderInfo.setCouponId(createOrderRequest.getCouponId());
        orderInfo.setPayTime(null);

        Long currentTimeMillis = System.currentTimeMillis();

        Integer expireTime = orderProperties.getExpireTime();

        orderInfo.setExpireTime(LocalDateTime.ofEpochSecond(currentTimeMillis + expireTime / 1000,0, ZoneOffset.ofHours(8)));

        orderInfo.setUserRemark(createOrderRequest.getUserRemark());

        orderInfo.setDeleteStatus(DeleteStatusEnum.NO.getCode());

        orderInfo.setCommentStauts(CommentStatusEnum.NO.getCode());
        fullOrderData.setOrderInfo(orderInfo);
        return this;
    }

    /** 
     * @description: 构建OrderItem对象
     * @param
     * @return  com.taki.order.bulider.NewOrderBuilder
     * @author Long
     * @date: 2022/1/7 9:44
     */ 
    public NewOrderBuilder buildOrderItems(){
        String orderId = createOrderRequest.getOrderId();
        String sellerId = createOrderRequest.getSellerId();

        List<CreateOrderRequest.OrderItemRequest> orderItemRequests = createOrderRequest.getOrderItemRequests();
        
        List<OrderItemDO> orderItems = new ArrayList<>();

        int num = 0;

        for (ProductSkuDTO productSkuDTO : productSkuList) {
            OrderItemDO orderItemDO = new OrderItemDO();
            orderItemDO.setOrderId(orderId);
            orderItemDO.setOrderItemId(genOrderItemId(orderId, ++num));
            orderItemDO.setProductType(productSkuDTO.getProductType());
            orderItemDO.setProductId(productSkuDTO.getProductId());
            orderItemDO.setProductImg(productSkuDTO.getProductId());
            orderItemDO.setProductName(productSkuDTO.getProductName());
            orderItemDO.setSkuCode(productSkuDTO.getSkuCode());


            for (CreateOrderRequest.OrderItemRequest orderItemRequest : orderItemRequests) {
                if (orderItemRequest.getSkuCode().equals(productSkuDTO.getSkuCode())) {
                    orderItemDO.setSaleQuantity(orderItemRequest.getSaleQuantity());
                    break;
                }
            orderItemDO.setSalePrice(productSkuDTO.getSalePrice());
            orderItemDO.setOriginAmount(orderItemDO.getSalePrice().multiply(new BigDecimal(orderItemDO.getSaleQuantity())));

            // 商品项目实际支付金额，默认是originAmount，当是 有优惠 抵扣 的时候需要 分摊
            BigDecimal realAmount = BigDecimal.ZERO;
            List<OrderAmountDetailDO> orderAmountDetails = ObjectUtil.convertList(calculateOrderAmount.getOrderAmountDetailDTOList(),OrderAmountDetailDO.class);

            // 判断 是否存在优惠券抵扣费用
             orderAmountDetails = orderAmountDetails.stream().filter(
                     orderAmountDetail -> orderAmountDetail.getSkuCode().equals(productSkuDTO.getSkuCode()) ).collect(Collectors.toList());


            if (!orderAmountDetails.isEmpty()){

                Map<Integer,OrderAmountDetailDO> orderAmountDetailDOMap = orderAmountDetails.stream().collect(
                        Collectors.toMap(OrderAmountDetailDO::getAmountType, Function.identity()));

                if (orderAmountDetailDOMap.get(AmountTypeEnum.COUPON_DISCOUNT_AMOUNT.getCode()) != null ){

                    realAmount = orderItemDO.getOriginAmount().subtract(
                            orderAmountDetailDOMap.get(AmountTypeEnum.COUPON_DISCOUNT_AMOUNT.getCode()).getAmount());

                }
            }

            if (realAmount.compareTo(BigDecimal.ZERO) > 0){
                orderItemDO.setPayAmount(realAmount);
            }else{
                orderItemDO.setPayAmount(orderItemDO.getOriginAmount());
            }
            orderItemDO.setProductUnit(productSkuDTO.getProductUnit());
            orderItemDO.setPurchasePrice(productSkuDTO.getPurchasePrice());
            orderItemDO.setSellerId(sellerId);
            orderItems.add(orderItemDO);

            }
        }
        fullOrderData.setOrderItems(orderItems);
        return this;
    }

    /***
     * @description: 构建订单收货地址
     * @param
     * @return  com.taki.order.bulider.NewOrderBuilder
     * @author Long
     * @date: 2022/1/7 10:36
     */
    public NewOrderBuilder buildOrderDeliveryDetail(){
        OrderDeliveryDetailDO orderDeliveryDetail = new OrderDeliveryDetailDO();
        orderDeliveryDetail.setOrderId(createOrderRequest.getOrderId());
        orderDeliveryDetail.setDeliveryType(createOrderRequest.getDeliveryType());
        orderDeliveryDetail.setProvince(createOrderRequest.getProvince());
        orderDeliveryDetail.setCity(createOrderRequest.getCity());
        orderDeliveryDetail.setArea(createOrderRequest.getArea());
        orderDeliveryDetail.setStreet(createOrderRequest.getStreet());
        orderDeliveryDetail.setDetailAddress(createOrderRequest.getDetailAddress());
        orderDeliveryDetail.setLon(createOrderRequest.getLon());
        orderDeliveryDetail.setLat(createOrderRequest.getLat());
        orderDeliveryDetail.setReceiverName(createOrderRequest.getReceiverName());
        orderDeliveryDetail.setReceiverPhone(createOrderRequest.getReceiverPhone());
        orderDeliveryDetail.setModifyAddressCount(0);

        fullOrderData.setOrderDeliveryDetailDO(orderDeliveryDetail);

        return this;
    }

    /** 
     * @description: 构建订单支付信息
     * @param 
     * @return  com.taki.order.bulider.NewOrderBuilder
     * @author Long
     * @date: 2022/1/7 10:46
     */ 
    public NewOrderBuilder buildOrderPaymentDetail(){

        List<CreateOrderRequest.PaymentRequest> paymentRequests = createOrderRequest.getPaymentRequests();

        List<OrderPaymentDetailDO> orderPaymentDetails = new ArrayList<>();

        paymentRequests.forEach(paymentRequest -> {
            OrderPaymentDetailDO orderPaymentDetail = new OrderPaymentDetailDO();
        orderPaymentDetail.setOrderId(createOrderRequest.getOrderId());
        orderPaymentDetail.setAccountType(paymentRequest.getAccountType());
        orderPaymentDetail.setPayType(paymentRequest.getPayType());
        orderPaymentDetail.setPayStatus(PayStatusEnum.UNPAID.getCode());
        List<CreateOrderRequest.OrderAmountRequest> orderAmountRequests = createOrderRequest.getOrderAmountRequests();

        Map<Integer,BigDecimal> orderAmountMap = orderAmountRequests.stream().collect(Collectors.toMap(
                CreateOrderRequest.OrderAmountRequest::getAmountType, CreateOrderRequest.OrderAmountRequest::getAmount));

        orderPaymentDetail.setPayAmount(orderAmountMap.get(AmountTypeEnum.REAL_PAY_AMOUNT.getCode()));
        orderPaymentDetail.setPayTime(null);
        orderPaymentDetail.setOutTradeNo(null);
        orderPaymentDetail.setPayRemark(null);

        orderPaymentDetails.add(orderPaymentDetail);

        });
        fullOrderData.setOrderPaymentDetails(orderPaymentDetails);
        return this;
    }
    
    /** 
     * @description: 构造订单费用信息
     * @param 
     * @return  com.taki.order.bulider.NewOrderBuilder
     * @author Long
     * @date: 2022/1/7 11:02
     */ 
    public NewOrderBuilder buildOrderAmount(){
        List<OrderAmountDTO>  orderAmountDTOList = ObjectUtil.convertList(calculateOrderAmount.getOrderAmountDTOList(),OrderAmountDTO.class);

        List<OrderAmountDO> orderAmounts = new ArrayList<>();

        orderAmountDTOList.forEach(orderAmountDTO -> {
            OrderAmountDO orderAmountDO = new OrderAmountDO();
            orderAmountDO.setOrderId(createOrderRequest.getOrderId());
            orderAmountDO.setAmount(orderAmountDTO.getAmount());
            orderAmountDO.setAmountType(orderAmountDTO.getAmountType());
            orderAmounts.add(orderAmountDO);
        });
        fullOrderData.setOrderAmounts(orderAmounts);

        return  this;
    }

    /** 
     * @description: 构造订单费用详细信息
     * @param 
     * @return  com.taki.order.bulider.NewOrderBuilder
     * @author Long
     * @date: 2022/1/7 11:33
     */ 
    public NewOrderBuilder buildOrderAmountDetail(){
        List<OrderAmountDetailDTO> orderAmountDetailList = ObjectUtil.convertList(calculateOrderAmount.getOrderAmountDetailDTOList(),OrderAmountDetailDTO.class);
        List<OrderAmountDetailDO> orderAmountDetails = new ArrayList<>();

        orderAmountDetailList.forEach(orderAmountDetailDTO -> {
            OrderAmountDetailDO orderAmountDetailDO = new OrderAmountDetailDO();
            orderAmountDetailDO.setOrderId(createOrderRequest.getOrderId());
            orderAmountDetailDO.setProductType(orderAmountDetailDTO.getProductType());

            fullOrderData.getOrderItems().forEach(orderItemDO -> {
                if (orderItemDO.getSkuCode().equals(orderAmountDetailDO.getSkuCode())){
                    orderAmountDetailDO.setOrderItemId(orderItemDO.getOrderItemId());
                    orderAmountDetailDO.setProductId(orderItemDO.getProductId());
                }
            });

            orderAmountDetailDO.setSkuCode(orderAmountDetailDTO.getSkuCode());
            orderAmountDetailDO.setSaleQuantity(orderAmountDetailDTO.getSaleQuantity());
            orderAmountDetailDO.setSalePrice(orderAmountDetailDO.getSalePrice());
            orderAmountDetailDO.setAmountType(orderAmountDetailDTO.getAmountType());
            orderAmountDetailDO.setAmount(orderAmountDetailDTO.getAmount());
            orderAmountDetails.add(orderAmountDetailDO);
        });

        fullOrderData.setOrderAmountDetails(orderAmountDetails);
        return this;
    }

    /** 
     * @description: 构造 订单操作日志
     * @param 
     * @return  com.taki.order.bulider.NewOrderBuilder
     * @author Long
     * @date: 2022/1/7 11:48
     */ 
    public NewOrderBuilder buildOperateLog(){
        OrderOperateLogDO operateLogDO = new OrderOperateLogDO();
        operateLogDO.setOrderId(createOrderRequest.getOrderId());
        operateLogDO.setOperateType(OrderOperateTypeEnum.NEW_ORDER.getCode());
        operateLogDO.setPreStatus(OrderStatusEnum.NULL.getCode());
        operateLogDO.setCurrentStatus(OrderStatusEnum.CREATED.getCode());
        operateLogDO.setRemark(null);

        fullOrderData.setOrderOperateLog(operateLogDO);

        return this;
    }

    /** 
     * @description:  构造訂單快照
     * @param 
     * @return  com.taki.order.bulider.NewOrderBuilder
     * @author Long
     * @date: 2022/1/7 13:42
     */ 
    public  NewOrderBuilder buildOrderSnapsShot(){
        String orderId = createOrderRequest.getOrderId();
        String couponId = createOrderRequest.getCouponId();

        List<OrderSnapshotDO> orderSnapshots = new ArrayList<>();

       if(StringUtils.isNotBlank(couponId)){
        //優惠券信息
        OrderSnapshotDO orderCouponSnapshot = new OrderSnapshotDO();

        orderCouponSnapshot.setOrderId(orderId);
        orderCouponSnapshot.setSnapshotType(SnapshotTypeEnum.ORDER_COUPON.getCode());
        orderCouponSnapshot.setSnapshotJson(null);
        orderSnapshots.add(orderCouponSnapshot);
       }
        // 費用信息
        OrderSnapshotDO orderAmountSnapshot = new OrderSnapshotDO();

        orderAmountSnapshot.setOrderId(orderId);
        orderAmountSnapshot.setSnapshotType(SnapshotTypeEnum.ORDER_AMOUNT.getCode());
        orderAmountSnapshot.setSnapshotJson(null);
        orderSnapshots.add(orderAmountSnapshot);


        // 訂單條目信息
        OrderSnapshotDO orderItemSnapshot = new OrderSnapshotDO();

        orderItemSnapshot.setOrderId(orderId);
        orderItemSnapshot.setSnapshotType(SnapshotTypeEnum.ORDER_ITEM.getCode());
        orderItemSnapshot.setSnapshotJson(null);
        orderSnapshots.add(orderItemSnapshot);

        return this;
    }


    public  FullOrderData build(){
        return fullOrderData;
    }

    /**
     * 生成 订单条目Id
     * @param orderId 订单Id
     * @param num 自增数
     * @return
     */
    private String genOrderItemId(String orderId, int num) {

        if (num < 10){
            return orderId +"_00" + num;
        }

        if (num < 100){
            return orderId + "_0" + num;
        }

        return orderId + "_" + num;
    }



}
