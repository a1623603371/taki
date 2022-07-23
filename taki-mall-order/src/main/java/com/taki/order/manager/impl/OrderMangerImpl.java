package com.taki.order.manager.impl;

import com.taki.address.api.AddressApi;
import com.taki.address.domian.dto.AddressDTO;
import com.taki.address.domian.request.AddressQuery;
import com.taki.common.enums.AmountTypeEnum;
import com.taki.common.enums.OrderOperateTypeEnum;
import com.taki.common.enums.OrderStatusEnum;
import com.taki.common.utlis.JsonUtil;
import com.taki.common.utlis.ObjectUtil;
import com.taki.common.utlis.ResponseData;
import com.taki.inventory.api.InventoryApi;
import com.taki.inventory.domain.request.DeductProductStockRequest;
import com.taki.market.api.MarketApi;
import com.taki.market.domain.dto.CalculateOrderAmountDTO;
import com.taki.market.domain.dto.UserCouponDTO;
import com.taki.market.domain.query.UserCouponQuery;
import com.taki.market.request.LockUserCouponRequest;
import com.taki.order.bulider.FullOrderData;
import com.taki.order.bulider.NewOrderBuilder;
import com.taki.order.config.OrderProperties;
import com.taki.order.dao.*;
import com.taki.order.domain.entity.*;
import com.taki.order.domain.request.CreateOrderRequest;
import com.taki.order.domain.request.PayCallbackRequest;
import com.taki.order.enums.PayStatusEnum;
import com.taki.order.enums.SnapshotTypeEnum;
import com.taki.order.exception.OrderBizException;
import com.taki.order.manager.OrderManager;
import com.taki.order.manager.OrderNoManager;
import com.taki.order.service.impl.NewOrderDataHolder;
import com.taki.product.domian.dto.ProductSkuDTO;
import io.seata.spring.annotation.GlobalTransactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName OrderMangerImpl
 * @Description 订单 组件
 * @Author Long
 * @Date 2022/5/9 21:13
 * @Version 1.0
 */
@Service
@Slf4j
public class OrderMangerImpl implements OrderManager {




    @Autowired
    private OrderProperties orderProperties;


    @Autowired
    private OrderAmountDao orderAmountDao;

    @Autowired
    private OrderAmountDetailDao orderAmountDetailDao;

    @Autowired
    private OrderSnapshotDao orderSnapshotDao;

    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private OrderDeliveryDetailDao orderDeliveryDetailDao;

    @Autowired
    private OrderItemDao orderItemDao;

    @Autowired
    private OrderNoManager orderNoManager;

    @Autowired
    private OrderPaymentDetailDao orderPaymentDetailDao;

    @Autowired
    private OrderOperateLogDao orderOperateLogDao;
    /**
     * 库存服务
     */
    @DubboReference(version ="1.0.0" ,retries = 0)
    private InventoryApi inventoryApi;

    /**
     * 营销服务
     */
    @DubboReference(version = "1.0.0" ,retries = 0)
    private MarketApi marketApi;


    @DubboReference(version = "1.0.0" ,retries = 0)
    private AddressApi addressApi;

    @Override
    @GlobalTransactional(rollbackFor = Exception.class)
    public Boolean createOrder(CreateOrderRequest createOrderRequest, List<ProductSkuDTO> productSkus, CalculateOrderAmountDTO calculateOrderAmount) {

        // 1锁定优惠券
        lockUserCoupon(createOrderRequest);



        //2. 扣减商品库存
        deductProductStock(createOrderRequest);


        // 2 生成订单
        addNewOrder(createOrderRequest,productSkus,calculateOrderAmount);


        return true;
    }

    @Override
    public void updateOrderStatusWhenPayCallback(OrderInfoDO orderInfo) {

        //更新 主单 的状态
        updateMasterOrderStatus(orderInfo);

        // 判断是否存在子订单
        String orderId = orderInfo.getOrderId();
        List<OrderInfoDO> subOrderInfoList = orderInfoDao.listByParentOrderId(orderId);

        if (CollectionUtils.isEmpty(subOrderInfoList)){
            return;
        }

        //更新子订单
        updateSubOrderStatus(orderInfo,subOrderInfoList);

    }

    /**
     * 更新子订单状态
     * @param orderInfo 订单信息
     * @param subOrderInfoList 子订单集合
     */
    private void updateSubOrderStatus(OrderInfoDO orderInfo, List<OrderInfoDO> subOrderInfoList) {
        String orderId = orderInfo.getOrderId();
        Integer perOrderStatus = orderInfo.getOrderStatus();
        Integer currentOrderStatus = OrderStatusEnum.INVALID.getCode();

        //先将主订单设置为无效订单
        List<String> orderIdList = Collections.singletonList(orderId);

        updateOrderStatus(orderIdList,currentOrderStatus);

        //新增 订单状态日志变更记录
        Integer operateType = OrderOperateTypeEnum.PAID_ORDER.getCode();

        String remark  = "订单支付回调操作,主订单状态变更" + perOrderStatus + "-" + currentOrderStatus;

        saveOrderOperateLog(orderId,operateType,perOrderStatus,currentOrderStatus,remark);

        // 在更新子订单 的状态

        Integer subCurrentOrderStatus = OrderStatusEnum.PAID.getCode();

        List<String> subOrderIdList = subOrderInfoList.stream().map(OrderInfoDO::getOrderId).collect(Collectors.toList());

        //更新子订单状态
        updateOrderStatus(subOrderIdList,subCurrentOrderStatus);

        // 更新子订单的支付明细
        updateOrderPayStatus(subOrderIdList,PayStatusEnum.PAID.getCode());

        //保存子订单操作日志
        saveSubOrderOperateLog(subCurrentOrderStatus,subOrderInfoList);


    }

    /** 
     * @description: 保存 子订单操作日志
     * @param subCurrentOrderStatus 子订单当前变更的状态
     * @param subOrderInfoList 子订单数据集合
     * @return  void
     * @author Long
     * @date: 2022/6/8 22:10
     */ 
    private void saveSubOrderOperateLog(Integer subCurrentOrderStatus, List<OrderInfoDO> subOrderInfoList) {

        List<OrderOperateLogDO> orderOperateLogDOList = new ArrayList<>();

        subOrderInfoList.forEach(subOrderInfo ->{
            String orderId = subOrderInfo.getOrderId();
            Integer preOrderStatus = subOrderInfo.getOrderStatus();

            // 订单状态变更日志
            OrderOperateLogDO  operateLogDO = new OrderOperateLogDO();
            operateLogDO.setOrderId(orderId);
            operateLogDO.setOperateType(OrderOperateTypeEnum.PAID_ORDER.getCode());
            operateLogDO.setPreStatus(preOrderStatus);
            operateLogDO.setCurrentStatus(subCurrentOrderStatus);
            String remark = "订单支付回调操作，子订单状态变更" + preOrderStatus + "-" + subCurrentOrderStatus;
            operateLogDO.setRemark(remark);
            orderOperateLogDOList.add(operateLogDO);
        });

        if (!orderOperateLogDOList.isEmpty()) {
            orderOperateLogDao.saveBatch(orderOperateLogDOList);
        }
    }

    /**
     * @description: 更新主订单状态
     * @param orderInfo 订单
     * @return  void
     * @author Long
     * @date: 2022/6/8 21:35
     */
    private void updateMasterOrderStatus(OrderInfoDO orderInfo) {

        String orderId  = orderInfo.getOrderId();
        // 更新主单状态
        Integer preOrderStatus = orderInfo.getOrderStatus();

        Integer currentStatus = OrderStatusEnum.PAID.getCode();

        List<String> orderIdList = Collections.singletonList(orderId);

        // 更新主单状态
        updateOrderStatus(orderIdList,currentStatus);


        updateOrderPayStatus(orderIdList, PayStatusEnum.PAID.getCode());


        //新增订单状态变更日志
        Integer operateType = OrderOperateTypeEnum.PAID_ORDER.getCode();
        String remark = "订单支付回调操作" + preOrderStatus + "-" + currentStatus;
        saveOrderOperateLog(orderId,operateType,preOrderStatus,currentStatus,remark);

        // 在更新


    }

    /**
     * @description: 更新 支付状态
     * @param orderIdList 订单id 集合
     * @param payStatus 支付状态
     * @return  void
     * @author Long
     * @date: 2022/6/8 21:51
     */
    private void updateOrderPayStatus(List<String> orderIdList, Integer payStatus) {
        OrderPaymentDetailDO orderPaymentDetailDO = new OrderPaymentDetailDO();
        orderPaymentDetailDO.setPayStatus(payStatus);

        if (orderIdList.size() == 1){
            orderPaymentDetailDao.updateByOrderId(orderPaymentDetailDO,orderIdList.get(0));
        }else {
            orderPaymentDetailDao.updateBatchByOrderId(orderPaymentDetailDO,orderIdList);
        }

    }

    /**
     * @description: 保存订单操作日志
     * @param orderId 订单id
     * @param  operateType 操作类型
     * @param preOrderStatus 变更之前状态
     * @param currentStatus  变更状态
     * @param remark 操作日志
     * @return  void
     * @author Long
     * @date: 2022/6/8 21:43
     */
    private void saveOrderOperateLog(String orderId, Integer operateType, Integer preOrderStatus, Integer currentStatus, String remark) {

        OrderOperateLogDO orderOperateLog = OrderOperateLogDO.builder()
                .orderId(orderId)
                .operateType(operateType)
                .preStatus(preOrderStatus)
                .currentStatus(currentStatus)
                .remark(remark)
                .build();
        orderOperateLogDao.save(orderOperateLog);
    }

    /**
     * @description:  更新订单状态
     * @param orderIdList 订单id 集合
     * @param  orderStatus 当前状态
     * @return  void
     * @author Long
     * @date: 2022/6/8 21:38
     */
    private void updateOrderStatus(List<String> orderIdList, Integer orderStatus) {
        OrderInfoDO orderInfoDO = new OrderInfoDO();
        orderInfoDO.setOrderStatus(orderStatus);

        if (orderIdList.size() == 1){
            orderInfoDao.updateByOrderId(orderInfoDO,orderIdList.get(0));

        }else {
            orderInfoDao.updateBatchByOrderId(orderInfoDO,orderIdList);
        }
    }


    /**
     * @description: 锁定商品库存
     * @param createOrderRequest  生单请求
     * @return  void
     * @author Long
     * @date: 2022/1/6 10:12
     */
    private void deductProductStock(CreateOrderRequest createOrderRequest) {

        String orderId = createOrderRequest.getOrderId();

        List<DeductProductStockRequest.OrderItemRequest> orderItemRequests = ObjectUtil.convertList(createOrderRequest.getOrderItemRequests(), DeductProductStockRequest.OrderItemRequest.class);

        DeductProductStockRequest  deductProductStockRequest= createOrderRequest.clone(DeductProductStockRequest.class);

        deductProductStockRequest.setOrderItemRequests(orderItemRequests);

        ResponseData<Boolean> responseResult = inventoryApi.deductProductStock(deductProductStockRequest);

        if (!responseResult.getSuccess()) {
            log.error("锁定商品仓库失败,订单号：{}", orderId);

            throw new OrderBizException(responseResult.getCode(), responseResult.getMessage());
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
     * @description: 添加订单到数据库
     * @param createOrderRequest 创建订单请求
     * @param productSkus 商品SKU 集合
     * @param calculateOrderAmount 订单费用
     * @return  void
     * @author Long
     * @date: 2022/1/6 10:39
     */
    private void addNewOrder(CreateOrderRequest createOrderRequest, List<ProductSkuDTO> productSkus, CalculateOrderAmountDTO calculateOrderAmount) {

        // 封装订单数据
        NewOrderDataHolder newOrderDataHolder = new NewOrderDataHolder();

        // 生成主订单
        FullOrderData fullOrderData = addNewMasterOrder(createOrderRequest,productSkus,calculateOrderAmount);


        //封装主订单数据到NewOrderData对象中
        newOrderDataHolder.appendOrderData(fullOrderData);

        // 多商品进行拆单
        Map<Integer,List<ProductSkuDTO>> productTypeMap = productSkus.stream().collect(Collectors.groupingBy(ProductSkuDTO::getProductType));

        if (productTypeMap.keySet().size() >  1){

            productTypeMap.keySet().forEach(productType ->{
                // 生成子订单
                FullOrderData fullSubOrderData = addNewSubOrder(fullOrderData,productType);

                // 进行封装
                newOrderDataHolder.appendOrderData(fullSubOrderData);
            });

            // 保存 订单到数据库
            List<OrderInfoDO> orderInfoDOList = newOrderDataHolder.getOrderInfos();

            if (!orderInfoDOList.isEmpty()){
                orderInfoDao.saveBatch(orderInfoDOList);
            }

            // 保存订单 条目 到数据库
            List<OrderItemDO> orderItemDOList = newOrderDataHolder.getOrderItems();
            if (! orderItemDOList.isEmpty()){
                orderItemDao.saveBatch(orderItemDOList);
            }

            //保存订单地址详情到数据库
            List<OrderDeliveryDetailDO> orderDeliveryDetailList = newOrderDataHolder.getOrderDeliveryDetails();
            if (!orderDeliveryDetailList.isEmpty()){
                orderDeliveryDetailDao.saveBatch(orderDeliveryDetailList);
            }

            // 保存 支付信息
            List<OrderPaymentDetailDO> orderPaymentDetailList = newOrderDataHolder.getOrderPaymentDetails();
            if (!orderDeliveryDetailList.isEmpty()){
                orderPaymentDetailDao.saveBatch(orderPaymentDetailList);
            }

            // 保存订单费用信息
            List<OrderAmountDO> orderAmountList = newOrderDataHolder.getOrderAmounts();

            if (!orderAmountList.isEmpty()){
                orderAmountDao.saveBatch(orderAmountList);
            }

            // 保存订单费用详细信息
            List<OrderAmountDetailDO> orderAmountDetailList = newOrderDataHolder.getOrderAmountDetails();

            if (!orderAmountDetailList.isEmpty()){
                orderAmountDetailDao.saveBatch(orderAmountDetailList);
            }

            //保存订单操作日志
            List<OrderOperateLogDO> orderOperateLogList = newOrderDataHolder.getOrderOperateLogs();
            if (!orderOperateLogList.isEmpty()){
                orderOperateLogDao.saveBatch(orderOperateLogList);
            }
            // 保存订单快照信息
            List<OrderSnapshotDO> orderSnapshotList = newOrderDataHolder.getOrderSnapshots();

            if (!orderSnapshotList.isEmpty()){
                orderSnapshotDao.saveBatch(orderSnapshotList);
            }

        }
    }

    /**
     * @param fullOrderData 完整订单数据
     * @param productType   商品类型
     * @return
     * @description: 添加子订单
     * @author Long
     * @date: 2022/1/10 12:00
     */
    private FullOrderData addNewSubOrder(FullOrderData fullOrderData, Integer productType) {

        // 订单
        OrderInfoDO orderInfo = fullOrderData.getOrderInfo();

        // 订单条目
        List<OrderItemDO> orderItems = fullOrderData.getOrderItems();

        // 订单配送信息
        OrderDeliveryDetailDO orderDeliveryDetail = fullOrderData.getOrderDeliveryDetailDO();
        // 主订单 支付信息
        List<OrderPaymentDetailDO> orderPaymentDetails = fullOrderData.getOrderPaymentDetails();
        // 主订单费用信息
        List<OrderAmountDO> orderAmounts = fullOrderData.getOrderAmounts();
        // 主订单费用详情
        List<OrderAmountDetailDO> orderAmountDetails = fullOrderData.getOrderAmountDetails();
        // 订单操作日志
        OrderOperateLogDO operateLog = fullOrderData.getOrderOperateLog();
        // 订单快照 日志
        List<OrderSnapshotDO> orderSnapshots = fullOrderData.getOrderSnapshots();

        // 父订单号
        String parentOrderId = orderInfo.getOrderId();

        // 用户Id
        String userId = orderInfo.getUserId();

        // 生成子订单号
        String subOrderId = orderNoManager.genOrderId(productType, userId);

        //字订单全量数据
        FullOrderData subFullOrderData = new FullOrderData();

        // 过滤出当前商品类型的订单条目信息

        List<OrderItemDO> subOrderItems = orderItems.stream().filter(orderItemDO -> productType.equals(orderItemDO.getProductType())).collect(Collectors.toList());

        // 统计子订单 金额

        BigDecimal subOrderAmount = BigDecimal.ZERO;

        BigDecimal subRealPayAmount = BigDecimal.ZERO;

        for (OrderItemDO subOrderItem : subOrderItems) {
            subOrderAmount = subOrderAmount.add(subOrderItem.getOriginAmount());
            subRealPayAmount = subRealPayAmount.add(subOrderItem.getPayAmount());
        }

        // 订单信息 主信息
        OrderInfoDO newSubOrder = orderInfo.clone(OrderInfoDO.class);
        newSubOrder.setId(null);
        newSubOrder.setOrderId(subOrderId);
        newSubOrder.setParentOrderId(parentOrderId);
        newSubOrder.setOrderStatus(OrderStatusEnum.INVALID.getCode());
        newSubOrder.setTotalAmount(subOrderAmount);
        newSubOrder.setPayAmount(subRealPayAmount);
        subFullOrderData.setOrderInfo(newSubOrder);

        // 订单条目
        List<OrderItemDO> newSubOrderItems = new ArrayList<>();

        subOrderItems.forEach(subOrderItem -> {
            OrderItemDO orderItemDO = subOrderItem.clone(OrderItemDO.class);
            orderItemDO.setId(null);
            orderItemDO.setOrderId(subOrderId);
            String subOrderItemId = getSubOrderItemId(orderItemDO.getOrderItemId(), subOrderId);
            orderItemDO.setOrderItemId(subOrderItemId);
            newSubOrderItems.add(subOrderItem);
        });
        subFullOrderData.setOrderItems(newSubOrderItems);

        // 订单配送地址信息
        OrderDeliveryDetailDO newOrderDeliveryDetail = orderDeliveryDetail.clone(OrderDeliveryDetailDO.class);
        newOrderDeliveryDetail.setId(null);
        newOrderDeliveryDetail.setOrderId(subOrderId);
        subFullOrderData.setOrderDeliveryDetailDO(newOrderDeliveryDetail);

        Map<String, OrderItemDO> subOrderItemMap = subOrderItems.stream().collect(Collectors.toMap(OrderItemDO::getOrderItemId, Function.identity()));

        // 子订单 总支付金额
        BigDecimal subTotalOriginPayAmount = BigDecimal.ZERO;
        // 子订单优惠券抵扣金额
        BigDecimal subTotalCouponDiscountAmount = BigDecimal.ZERO;
        // 子订单 实际支付金额
        BigDecimal subTotalRealPayAmount = BigDecimal.ZERO;

        List<OrderAmountDetailDO> subOrderAmountDetailList = new ArrayList<>();


        for (OrderAmountDetailDO orderAmountDetail : orderAmountDetails) {

            String orderItemId = orderAmountDetail.getOrderItemId();

            if (!subOrderItemMap.containsKey(orderItemId)) {
                continue;
            }

            OrderAmountDetailDO subOrderAmountDetail = orderAmountDetail.clone(OrderAmountDetailDO.class);
            subOrderAmountDetail.setId(null);
            subOrderAmountDetail.setOrderId(subOrderId);
            String subOrderItemId = getSubOrderItemId(orderItemId, subOrderId);
            subOrderAmountDetail.setOrderItemId(subOrderItemId);
            subOrderAmountDetailList.add(subOrderAmountDetail);

            Integer amountType = orderAmountDetail.getAmountType();
            BigDecimal amount = orderAmountDetail.getAmount();

            if (AmountTypeEnum.ORIGIN_PAY_AMOUNT.getCode().equals(amountType)) {
                subTotalOriginPayAmount = subTotalOriginPayAmount.add(amount);
            }

            if (AmountTypeEnum.REAL_PAY_AMOUNT.getCode().equals(amountType)) {
                subTotalRealPayAmount = subTotalRealPayAmount.add(amount);
            }

            if (AmountTypeEnum.COUPON_DISCOUNT_AMOUNT.getCode().equals(amountType)) {
                subTotalCouponDiscountAmount = subTotalCouponDiscountAmount.add(amount);
            }

        }

        subFullOrderData.setOrderAmountDetails(subOrderAmountDetailList);


        // 订单费用
        List<OrderAmountDO> subOrderAmountList = new ArrayList<>();


        for (OrderAmountDO orderAmountDO : orderAmounts) {

            Integer amountType = orderAmountDO.getAmountType();

            OrderAmountDO subOrderAmountDO = orderAmountDO.clone(OrderAmountDO.class);

            subOrderAmountDO.setId(null);
            subOrderAmountDO.setOrderId(subOrderId);

            if (amountType.equals(AmountTypeEnum.ORIGIN_PAY_AMOUNT.getCode())) {
                subOrderAmountDO.setAmount(subTotalOriginPayAmount);
                subOrderAmountList.add(subOrderAmountDO);
            }

            if (amountType.equals(AmountTypeEnum.REAL_PAY_AMOUNT.getCode())) {
                subOrderAmountDO.setAmount(subRealPayAmount);
                subOrderAmountList.add(subOrderAmountDO);
            }

            if (amountType.equals(AmountTypeEnum.COUPON_DISCOUNT_AMOUNT.getCode())) {
                subOrderAmountDO.setAmount(subTotalCouponDiscountAmount);
                subOrderAmountList.add(subOrderAmountDO);
            }

        }

        subFullOrderData.setOrderAmounts(subOrderAmountList);

        // 订单支付信息
        List<OrderPaymentDetailDO> subOrderPaymentDetails = new ArrayList<>();

        for (OrderPaymentDetailDO orderPaymentDetailDO : orderPaymentDetails) {
            OrderPaymentDetailDO subOrderPaymentDetailDO = orderPaymentDetailDO.clone(OrderPaymentDetailDO.class);
            subOrderPaymentDetailDO.setId(null);
            subOrderPaymentDetailDO.setOrderId(subOrderId);
            subOrderPaymentDetailDO.setPayAmount(subTotalRealPayAmount);
            subOrderPaymentDetails.add(subOrderPaymentDetailDO);
        }

        // 订单状态变更
        OrderOperateLogDO suborderOperateLogDO = operateLog.clone(OrderOperateLogDO.class);
        suborderOperateLogDO.setId(null);
        suborderOperateLogDO.setOrderId(subOrderId);
        subFullOrderData.setOrderOperateLog(suborderOperateLogDO);

        // 订单商品快照
        List<OrderSnapshotDO> subOrderSnapshotList = new ArrayList<>();
        orderSnapshots.forEach(orderSnapshotDO -> {
            OrderSnapshotDO subOrderSnapshotDO = orderSnapshotDO.clone(OrderSnapshotDO.class);
            subOrderSnapshotDO.setId(null);
            subOrderSnapshotDO.setOrderId(subOrderId);
            if (SnapshotTypeEnum.ORDER_AMOUNT.equals(orderSnapshotDO.getSnapshotType())){
                subOrderSnapshotDO.setSnapshotJson(JsonUtil.object2Json(subOrderAmountList));
            }
            if (SnapshotTypeEnum.ORDER_ITEM.equals(orderSnapshotDO.getSnapshotType())){
                subOrderSnapshotDO.setSnapshotJson(JsonUtil.object2Json(subOrderItems));
            }

            subOrderSnapshotList.add(subOrderSnapshotDO);

        });
        subFullOrderData.setOrderSnapshots(subOrderSnapshotList);

        return subFullOrderData;
    }

    /**
     * @description: 获取子订单的orderItemId
     * @param
     * @return  java.lang.String
     * @author Long
     * @date: 2022/1/10 13:58
     */
    private String getSubOrderItemId(String orderItemId ,String  subOrderId) {
        String postfix = orderItemId.substring(orderItemId.indexOf("_"));
        return subOrderId + postfix;
    }
    /**
     * @description: 生成主订单
     * @param createOrderRequest 创建订单请求
     * @param productSkus 商品SKU
     * @param calculateOrderAmount 订单费用
     * @return  主订单数据
     * @author Long
     * @date: 2022/1/8 13:51
     */
    private FullOrderData addNewMasterOrder(CreateOrderRequest createOrderRequest, List<ProductSkuDTO> productSkus, CalculateOrderAmountDTO calculateOrderAmount) {

        NewOrderBuilder newOrderBuilder = new NewOrderBuilder(createOrderRequest,productSkus,calculateOrderAmount,orderProperties);
        FullOrderData fullOrderData = newOrderBuilder.builder()
                .buildOrderItems()
                .buildOrderAmount()
                .buildOrderAmountDetail()
                .buildOrderDeliveryDetail()
                .buildOrderPaymentDetail()
                .buildOperateLog()
                .buildOrderSnapsShot()
                .build();

        // 订单信息
        OrderInfoDO orderInfo =fullOrderData.getOrderInfo();

        // 订单条目
        List<OrderItemDO> orderItems = fullOrderData.getOrderItems();

        // 订单费用信息
        List<OrderAmountDO> orderAmounts = fullOrderData.getOrderAmounts();

        //补全地址信息
        OrderDeliveryDetailDO orderDeliveryDetail = fullOrderData.getOrderDeliveryDetailDO();
        String detailAddress = getDeliveryDetail(orderDeliveryDetail);
        orderDeliveryDetail.setDetailAddress(detailAddress);

        //补全订单状态变更日志

        OrderOperateLogDO orderOperateLog = fullOrderData.getOrderOperateLog();

        String remark = "创建订单操作0-10";
        orderOperateLog.setRemark(remark);

        //补全商品快照信息

        List<OrderSnapshotDO> orderSnapshots = fullOrderData.getOrderSnapshots();

        orderSnapshots.forEach(orderSnapshotDO -> {

            // 优惠券信息
            if (orderSnapshotDO.getSnapshotType().equals(SnapshotTypeEnum.ORDER_COUPON.getCode())){
                String couponId = orderInfo.getCouponId();
                String userId = orderInfo.getUserId();
                UserCouponQuery userCouponQuery = new UserCouponQuery();
                userCouponQuery.setCouponId(couponId);
                userCouponQuery.setUserId(userId);

                ResponseData<UserCouponDTO> responseResult = marketApi.queryUserCoupon(userCouponQuery);

                if (responseResult.getSuccess()){
                    UserCouponDTO userCoupon = responseResult.getData();
                    if (userCoupon != null){
                        orderSnapshotDO.setSnapshotJson(JsonUtil.object2Json(userCoupon));

                    }

                }else {
                    orderSnapshotDO.setSnapshotJson(JsonUtil.object2Json(couponId));
                }
            }

            // 订单费用

            if(orderSnapshotDO.getSnapshotType().equals(SnapshotTypeEnum.ORDER_AMOUNT.getCode())){

                orderSnapshotDO.setSnapshotJson(JsonUtil.object2Json(orderAmounts));
            }

            // 订单条目
            if (orderSnapshotDO.getSnapshotType().equals(SnapshotTypeEnum.ORDER_ITEM.getCode())){
                orderSnapshotDO.setSnapshotJson(JsonUtil.object2Json(orderItems));
            }

        });

        return fullOrderData;
    }


    /**
     * 获取手收货地址详细地址
     * @param orderDeliveryDetail 订单地址信息
     * @return
     */
    private String getDeliveryDetail(OrderDeliveryDetailDO orderDeliveryDetail) {

        String provinceCode = orderDeliveryDetail.getProvince();
        String cityCode = orderDeliveryDetail.getCity();
        String areaCode = orderDeliveryDetail.getArea();
        String streetCode = orderDeliveryDetail.getStreet();
        AddressQuery addressQuery = new AddressQuery();

        addressQuery.setProvinceCode(provinceCode);
        addressQuery.setCityCode(cityCode);
        addressQuery.setAreaCode(areaCode);
        addressQuery.setStreetCode(streetCode);
        ResponseData<AddressDTO> responseResult = addressApi.queryAddress(addressQuery);

        if (!responseResult.getSuccess()  || ObjectUtils.isEmpty(responseResult.getData())){
            return orderDeliveryDetail.getDetailAddress();
        }
        AddressDTO address = responseResult.getData();

        StringBuilder addressBuilder = new StringBuilder();

        if (StringUtils.isNotBlank(address.getProvince())){
            addressBuilder.append(address.getProvince());
        }

        if (StringUtils.isNotBlank(address.getCity())){
            addressBuilder.append(address.getCity());
        }

        if (StringUtils.isNotBlank(address.getArea())){
            addressBuilder.append(address.getArea());
        }

        if (StringUtils.isNotBlank(address.getStreet())){
            addressBuilder.append(address.getStreet());
        }
        return addressBuilder.toString();
    }

}
