package com.taki.order.service.impl;

import com.taki.common.core.CloneDirection;
import com.taki.common.enums.AmountTypeEnum;
import com.taki.common.enums.PayTypeEnum;
import com.taki.common.exception.ServiceException;
import com.taki.common.utlis.ObjectUtil;
import com.taki.common.utlis.ParamCheckUtil;
import com.taki.common.utlis.ResponseData;
import com.taki.inventory.api.InventoryApi;
import com.taki.inventory.domain.request.LockProductStockRequest;
import com.taki.market.api.MarketApi;
import com.taki.market.domain.dto.CalculateOrderAmountDTO;
import com.taki.market.request.CalculateOrderAmountRequest;
import com.taki.market.request.LockUserCouponRequest;
import com.taki.order.domin.dto.CreateOrderDTO;
import com.taki.order.domin.dto.GenOrderIdDTO;
import com.taki.order.domin.dto.OrderAmountDTO;
import com.taki.order.domin.dto.OrderAmountDetailDTO;
import com.taki.order.domin.request.CreateOrderRequest;
import com.taki.order.domin.request.GenOrderIdRequest;
import com.taki.order.enums.*;
import com.taki.order.exception.OrderBizException;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.manager.OrderNoManager;
import com.taki.order.service.OrderInfoService;
import com.taki.product.api.ProductApi;
import com.taki.product.domian.dto.ProductSkuDTO;
import com.taki.product.domian.query.ProductSkuQuery;
import com.taki.risk.api.RiskApi;
import com.taki.risk.domain.dto.CheckOrderRiskDTO;
import com.taki.risk.domain.request.CheckOrderRiskRequest;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName OrderInfoServiceImpl
 * @Description 订单 service接口实现
 * @Author Long
 * @Date 2022/1/2 17:07
 * @Version 1.0
 */
@Service
@Slf4j
public class OrderInfoServiceImpl implements OrderInfoService {

    @Autowired
    private OrderNoManager orderNoManager;



    @DubboReference
    private RiskApi riskApi;


    @DubboReference
    private ProductApi productApi;


    @DubboReference
    private MarketApi marketApi;


    @DubboReference
    private InventoryApi inventoryApi;

    @Override
    public GenOrderIdDTO getGenOrderIdDTO(GenOrderIdRequest genOrderIdRequest) {

        String userId = genOrderIdRequest.getUserId();
        ParamCheckUtil.checkStringNonEmpty(userId);

        Integer businessIdentifier = genOrderIdRequest.getBusinessIdentifier();
        ParamCheckUtil.checkObjectNonNull(businessIdentifier);
        String orderId = orderNoManager.genOrderId(OrderAutoTypeEnum.SALE_ORDER.getCode(),userId);
        GenOrderIdDTO genOrderId = new GenOrderIdDTO();
        genOrderId.setOrderId(orderId);
        log.info("生单id:{}",orderId);
        return genOrderId;
    }

    @GlobalTransactional(rollbackFor = Exception.class)
    @Override
    public CreateOrderDTO createOrder(CreateOrderRequest createOrderRequest) {

        // 1.入参检查
        checkCreateOrderRequestParam(createOrderRequest);

        // 2.风控检查
        checkRisk(createOrderRequest);

        // 3.查询商品
        List<ProductSkuDTO> productSkus = listProductSkus(createOrderRequest);

        // 4.计算订单商品价格
        CalculateOrderAmountDTO calculateOrderAmount = calculateOrderAmount(createOrderRequest,productSkus);

        // 5.验证订单实付金额
        checkRealPayAmount(createOrderRequest,calculateOrderAmount);

        //6. 锁定优惠券
        lockUserCoupon(createOrderRequest);

        //7. 锁定商品库存
        lockProductStock(createOrderRequest);
        //8. 生成订单到数据库
        addNewOrder(createOrderRequest,productSkus,calculateOrderAmount);

        return null;
    }
    /**
     * @description: 添加订单到数据库
     * @param createOrderRequest 创建订单请求
     * @param productSkus 商品SKU 集合
     * @param calculateOrderAmount 订单费用
     * @return  void
     * @author Long
     * @date: 2022/1/6 10:39
     */
    private void addNewOrder(CreateOrderRequest createOrderRequest, List<ProductSkuDTO> productSkus, CalculateOrderAmountDTO calculateOrderAmount) {
    }

    /** 
     * @description: 锁定商品库存
     * @param createOrderRequest  生单请求
     * @return  void
     * @author Long
     * @date: 2022/1/6 10:12
     */ 
    private void lockProductStock(CreateOrderRequest createOrderRequest) {

    String orderId = createOrderRequest.getOrderId();

    List<LockProductStockRequest.OrderItemRequest> orderItemRequests = ObjectUtil.convertList(createOrderRequest.getOrderItemRequests(),LockProductStockRequest.OrderItemRequest.class);

    LockProductStockRequest lockProductStockRequest = createOrderRequest.clone(LockProductStockRequest.class);

    lockProductStockRequest.setOrderItemRequests(orderItemRequests);

    ResponseData<Boolean> responseResult = inventoryApi.lockProductStock(lockProductStockRequest);

    if (!responseResult.getSuccess()){
        log.error("锁定商品仓库失败,订单号：{}",orderId);

        throw  new OrderBizException(responseResult.getCode(),responseResult.getMessage());
    }


    }

    /**
     * @description: 锁定优惠券
     * @param createOrderRequest 生单请求
     * @return  void
     * @author Long
     * @date: 2022/1/6 9:42
     */
    private void lockUserCoupon(CreateOrderRequest createOrderRequest) {

        String coupon = createOrderRequest.getCouponId();

        if (StringUtils.isBlank(coupon)){
                return;
        }

        LockUserCouponRequest lockUserCouponRequest = createOrderRequest.clone(LockUserCouponRequest.class);

        ResponseData<Boolean> responseResult = marketApi.lockUserCoupon(lockUserCouponRequest);

        if (!responseResult.getSuccess()){
            log.error("锁定优惠券失败,订单号:{},优惠券Id:{},用户Id:{}",lockUserCouponRequest.getOrderId(),lockUserCouponRequest.getCouponId(),lockUserCouponRequest.getUserId());
            throw new OrderBizException(responseResult.getCode(),responseResult.getMessage());
        }

    }

    /**
     * @description: 验证订单实付金额
     * @param createOrderRequest 创建订单请求
     * @param calculateOrderAmount 计算的订单费用
     * @return  void
     * @author Long
     * @date: 2022/1/6 9:17
     */ 
    private void checkRealPayAmount(CreateOrderRequest createOrderRequest, CalculateOrderAmountDTO calculateOrderAmount) {
        List<CreateOrderRequest.OrderAmountRequest> orderAmountRequests = createOrderRequest.getOrderAmountRequests();

        Map<Integer, CreateOrderRequest.OrderAmountRequest> originOrderAmountMap =orderAmountRequests.stream()
                .collect(Collectors.toMap(CreateOrderRequest.OrderAmountRequest::getAmountType,Function.identity()));

        BigDecimal originRealPayAmount = originOrderAmountMap.get(AmountTypeEnum.REAL_PAY_AMOUNT.getCode()).getAmount();

        Map<Integer,CalculateOrderAmountDTO.OrderAmountDTO> orderAmountMap = calculateOrderAmount.getOrderAmountDTOList().stream()
                .collect(Collectors.toMap(CalculateOrderAmountDTO.OrderAmountDTO::getAmountType,Function.identity()));

        // 营销计算的实付价格
        BigDecimal realPayAmount = orderAmountMap.get(AmountTypeEnum.REAL_PAY_AMOUNT.getCode()).getAmount();

        if (originRealPayAmount.compareTo(realPayAmount) != 0){
            log.error("验证订单实付价格不通过,前端传入价格:{},营销计算价格：{}",originRealPayAmount,realPayAmount);
            throw new OrderBizException(OrderErrorCodeEnum.ORDER_CHECK_REAL_PAY_AMOUNT_FAIL);
        }

    }

    /**
     * @description: 计算订单费用 计算 优惠券 ,红包， 积分
     * @param createOrderRequest 生单请求
     * @return 计算结果
     * @author Long
     * @date: 2022/1/5 10:20
     */
    private CalculateOrderAmountDTO calculateOrderAmount(CreateOrderRequest createOrderRequest,List<ProductSkuDTO> productSkuList) {
        CalculateOrderAmountRequest calculateOrderAmountRequest = createOrderRequest.clone(CalculateOrderAmountRequest.class, CloneDirection.FORWARD);

        // 订单条目补充商品信息
        Map<String,ProductSkuDTO> productSkuDTOMap = productSkuList.stream().collect(Collectors.toMap(ProductSkuDTO::getSkuCode, Function.identity()));


        calculateOrderAmountRequest.getOrderItemRequests().forEach(orderItemRequest -> {
            String skuCode = orderItemRequest.getSkuCode();
            ProductSkuDTO productSku = productSkuDTOMap.get(skuCode);
            productSku.setSalePrice(orderItemRequest.getSalePrice());
            productSku.setProductId(orderItemRequest.getProductId());
        });
        // 调用营销服务计算订单价格
        ResponseData<CalculateOrderAmountDTO> responseResult = marketApi.calculateOrderAmount(calculateOrderAmountRequest);

        if (responseResult.getSuccess()){
            log.error("调用营销服务计算价格失败错误信息：{}",responseResult.getMessage());
            throw new OrderBizException(responseResult.getCode(),responseResult.getMessage());
        }
        CalculateOrderAmountDTO calculateOrderAmount = responseResult.getData();

        if (!ObjectUtils.isNotEmpty(calculateOrderAmount)){
            log.error("计算订单价格失败");
            throw new OrderBizException(OrderErrorCodeEnum.CALCULATE_ORDER_AMOUNT_ERROR);
        }
        // 订单费用
        List<OrderAmountDTO> orderAmountDTOList = ObjectUtil.convertList(calculateOrderAmount.getOrderAmountDTOList(),OrderAmountDTO.class);

        if (orderAmountDTOList == null || orderAmountDTOList.isEmpty()){
            log.error("计算订单价格的订单费用信息为空");
            throw  new OrderBizException(OrderErrorCodeEnum.CALCULATE_ORDER_AMOUNT_ERROR);
        }

        List<OrderAmountDetailDTO> orderAmountDetailList = ObjectUtil.convertList(calculateOrderAmount.getOrderAmountDetailDTOList(),OrderAmountDetailDTO.class);

        if (orderAmountDetailList == null || orderAmountDetailList.isEmpty()){
            log.error("计算订单价格的订单费用详细信息为空");
            throw  new OrderBizException(OrderErrorCodeEnum.CALCULATE_ORDER_AMOUNT_ERROR);
        }

        return calculateOrderAmount;

    }

    /**
     * @description: 获取商品SKU
     * @param createOrderRequest 创建订单请求
     * @return  商品SKU 集合
     * @author Long
     * @date: 2022/1/4 11:32
     *
     */
    private List<ProductSkuDTO> listProductSkus(CreateOrderRequest createOrderRequest) {

        List<CreateOrderRequest.OrderItemRequest> itemRequests = createOrderRequest.getOrderItemRequests();

        List<ProductSkuDTO> productSkus = new ArrayList<>();

        itemRequests.forEach(orderItemRequest -> {
            ProductSkuQuery query = new ProductSkuQuery();
            query.setSkuCode(orderItemRequest.getSkuCode());
            query.setSellerId(query.getSellerId());
            ResponseData<ProductSkuDTO> responseResult = productApi.getProductSku(query);
            if (!responseResult.getSuccess()){
                log.error("调用商品RPC失败错误信息:{}",responseResult.getMessage());
                throw new OrderBizException(responseResult.getCode(),responseResult.getMessage());
            }
            ProductSkuDTO productSku = responseResult.getData();

            if (!ObjectUtils.isNotEmpty(productSku)){
                log.error("商品不存在SKU:{}",orderItemRequest.getSkuCode());
                throw new OrderBizException(OrderErrorCodeEnum.PRODUCT_SKU_CODE_ERROR, orderItemRequest.getSkuCode());
            }

            productSkus.add(productSku);
        });

        return productSkus;

    }

    /**
     * @description: 风控检查
     * @param createOrderRequest 生单请求
     * @return  void
     * @author Long
     * @date: 2022/1/4 9:37
     */
    private void checkRisk(CreateOrderRequest createOrderRequest) {
        CheckOrderRiskRequest checkOrderRiskRequest = createOrderRequest.clone(CheckOrderRiskRequest.class);

        ResponseData<CheckOrderRiskDTO> responseResult = riskApi.checkOrderRisk(checkOrderRiskRequest);
        if (!responseResult.getSuccess()){
            log.error("风控服务调用失败异常信息:{}",responseResult.getMessage());
            throw new OrderBizException(responseResult.getCode(),responseResult.getMessage());
        }

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

        Map<Integer,BigDecimal> orderAmountMap = orderAmountRequests.stream().collect(Collectors.toMap(CreateOrderRequest.OrderAmountRequest::getAmountType, CreateOrderRequest.OrderAmountRequest::getAmount));

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
