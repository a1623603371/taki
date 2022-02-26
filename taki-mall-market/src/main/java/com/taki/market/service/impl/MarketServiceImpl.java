package com.taki.market.service.impl;

import cn.hutool.db.sql.Order;
import com.taki.common.enums.AmountTypeEnum;
import com.taki.common.utlis.ObjectUtil;
import com.taki.common.utlis.ParamCheckUtil;
import com.taki.market.dao.CouponDao;
import com.taki.market.dao.FreightTemplateDao;
import com.taki.market.domain.dto.CalculateOrderAmountDTO;
import com.taki.market.domain.entity.CouponDO;
import com.taki.market.domain.entity.FreightTemplateDO;
import com.taki.market.exception.MarketBizException;
import com.taki.market.exception.MarketErrorCodeEnum;
import com.taki.market.request.CalculateOrderAmountRequest;
import com.taki.market.service.MarketService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @ClassName MarketServiceImpl
 * @Description 营销 service 组件
 * @Author Long
 * @Date 2022/2/18 15:48
 * @Version 1.0
 */
@Slf4j
@Service
public class MarketServiceImpl implements MarketService {

    @Autowired
    private CouponDao couponDao;


    @Autowired
    private FreightTemplateDao freightTemplateDao;


    /** 
     * @description: 计算订单费用
     *  假设订单有两条商品条目记录，分摊优惠券的规则如下：
     *商品1
     *单价（单位 元） 购买数量 小计
     * 100           10    10*10
     *
     *商品2
     *
     *单价   购买数量   小计
     * 10   1        1*10
     *
     *
     *
     * 整单优惠券抵扣 5元
     *
     * 则 购买 商品 1 分摊抵扣金额 为：
     * 使用优惠券 抵扣总金额 * （商品1 单价 * 商品1 购买数量） / ((商品1 单价  * 商品1 购买数量) + (商品2单价 * 商品2 购买数量) )
     *
     * = 5 *（100 * 10 ） / ((100 * 10) + (10 * 1)) =
     * = 5000 / 1010
     * = 4.95
     *
     * 同样的逻辑可以计算出商品2的优惠券抵扣金额为 5分
     *
     * 如果计算出优惠券分摊到一条 item 上存在小数时，则向上取整，然后最后一条 item 分摊的金额 就是用优惠金额减掉前面所有的优惠的item分摊的金额
     *
     *
     * @param calculateOrderAmountRequest 计算订单费用入参
     * @return
     * @author Long
     * @date: 2022/2/18 16:12
     */ 

    @Override
    public CalculateOrderAmountDTO calculateOrderAmount(CalculateOrderAmountRequest calculateOrderAmountRequest) {

        // 检查入参
        checkCalculateOrderAmountRequest(calculateOrderAmountRequest);


        String userId = calculateOrderAmountRequest.getUserId();
        String orderId = calculateOrderAmountRequest.getOrderId();
        String couponId = calculateOrderAmountRequest.getCouponId();
        String regionId = calculateOrderAmountRequest.getRegion();

    // 优惠券抵扣价格
        BigDecimal discountAmount = BigDecimal.ZERO;

        if (StringUtils.isNotBlank(couponId)){
            // 锁定优惠券
            CouponDO couponDO = getCoupAchieve(userId,couponId);
            discountAmount = couponDO.getAmount();
        }

        // 原订单费用信息
        List<CalculateOrderAmountDTO.OrderAmountDTO> orderAmounts = ObjectUtil.convertList(
                calculateOrderAmountRequest.getOrderAmountRequests(), CalculateOrderAmountDTO.OrderAmountDTO.class,orderAmountDTO -> {
                    orderAmountDTO.setOrderId(orderId);
                });
        // 订单 条目费用 信息
        List<CalculateOrderAmountDTO.OrderAmountDetailDTO> orderAmountDetails = new ArrayList<>();
        List<CalculateOrderAmountRequest.OrderItemRequest> orderItemRequests = calculateOrderAmountRequest.getOrderItemRequests();

        //统计商品 全部费用
        BigDecimal totalProductAmount = BigDecimal.ZERO;

        for (CalculateOrderAmountRequest.OrderItemRequest orderItemRequest : orderItemRequests) {
            BigDecimal productAmount = orderItemRequest.getSalePrice().multiply(new BigDecimal(orderItemRequest.getSaleQuantity()));
            totalProductAmount = totalProductAmount.add(productAmount);
        }

        int index = 0;
        // 订单条目数量
        int totalNum = orderItemRequests.size();
        // 最后总金额抵扣金额
        BigDecimal notLastItemTotalDiscountAmount = BigDecimal.ZERO;

        for (CalculateOrderAmountRequest.OrderItemRequest orderItemRequest : orderItemRequests) {
            // 订单条目支付原价
            CalculateOrderAmountDTO.OrderAmountDetailDTO  originPayAmountDetail = createOrderAmountDetailDTO(
            orderId, AmountTypeEnum.REAL_PAY_AMOUNT.getCode(),null,null,orderItemRequest);
            orderAmountDetails.add(originPayAmountDetail);

            // 优惠券抵扣价格
            CalculateOrderAmountDTO.OrderAmountDetailDTO couponDiscountAmountDetail;

            if (++index < totalNum){
                // 订单条目分摊的优惠金额
                BigDecimal productAmount = orderItemRequest.getSalePrice().multiply(new BigDecimal(orderItemRequest.getSaleQuantity()));
                // 商品分摊 优惠金额
                BigDecimal partDiscountAmount = discountAmount.multiply(productAmount).divide(totalProductAmount,2,BigDecimal.ROUND_HALF_UP);
                couponDiscountAmountDetail = createOrderAmountDetailDTO(
                        orderId,AmountTypeEnum.COUPON_DISCOUNT_AMOUNT.getCode(),partDiscountAmount,null,orderItemRequest);

                notLastItemTotalDiscountAmount =notLastItemTotalDiscountAmount.add(couponDiscountAmountDetail.getAmount());

            }else {
                couponDiscountAmountDetail = createOrderAmountDetailDTO(
                        orderId,AmountTypeEnum.COUPON_DISCOUNT_AMOUNT.getCode(),
                        notLastItemTotalDiscountAmount.subtract(discountAmount),
                        null,orderItemRequest);
            }
            orderAmountDetails.add(couponDiscountAmountDetail);

            // 实际支付
            BigDecimal realPayAmount = originPayAmountDetail.getAmount().subtract(couponDiscountAmountDetail.getAmount());

            CalculateOrderAmountDTO.OrderAmountDetailDTO realPayAmountDetail = createOrderAmountDetailDTO(
                    orderId,AmountTypeEnum.REAL_PAY_AMOUNT.getCode(),null,realPayAmount,orderItemRequest);
            orderAmountDetails.add(realPayAmountDetail);


            // 重新计算订单支付价格原价，优惠券抵扣金额，实际支付
            BigDecimal totalOriginPayAmount = BigDecimal.ZERO;
            BigDecimal totalDiscountAmount = BigDecimal.ZERO;
            BigDecimal totalRealPayAmount = BigDecimal.ZERO;

            for (CalculateOrderAmountDTO.OrderAmountDetailDTO orderAmountDetail : orderAmountDetails) {

                Integer amountType = orderAmountDetail.getAmountType();
                BigDecimal amount = orderAmountDetail.getAmount();

                if (AmountTypeEnum.ORIGIN_PAY_AMOUNT.getCode().compareTo(amountType) == 0){
                    totalOriginPayAmount =     totalOriginPayAmount.add(amount);
                }else if (AmountTypeEnum.COUPON_DISCOUNT_AMOUNT.getCode().compareTo(amountType) == 0){
                    totalDiscountAmount =    totalDiscountAmount.add(amount);
                }else if (AmountTypeEnum.REAL_PAY_AMOUNT.getCode().compareTo(amountType) == 0){
                    totalRealPayAmount =  totalRealPayAmount.add(amount);
                }
            }
                // 总支付实际金额 加运费
                Map<Integer, CalculateOrderAmountDTO.OrderAmountDTO> orderAmountMap =orderAmountMap =
                        orderAmounts.stream().collect(Collectors.toMap(
                                CalculateOrderAmountDTO.OrderAmountDTO::getAmountType, Function.identity()));
                // 运费
                BigDecimal shippingAmount = calculateOrderShippingAmount(regionId,orderAmountMap);

                if (ObjectUtils.isNotEmpty(shippingAmount)){
                    totalRealPayAmount =   totalRealPayAmount.add(shippingAmount);
                }

                for (CalculateOrderAmountDTO.OrderAmountDTO orderAmount : orderAmounts) {
                    Integer amountType = orderAmount.getAmountType();
                    BigDecimal amount = orderAmount.getAmount();
                    if (AmountTypeEnum.ORIGIN_PAY_AMOUNT.getCode().compareTo(amountType) == 0){
                        orderAmount.setAmount(totalOriginPayAmount);
                    }
                    if (AmountTypeEnum.REAL_PAY_AMOUNT.getCode().compareTo(amountType) == 0){
                        orderAmount.setAmount(totalRealPayAmount);
                    }
                    if (AmountTypeEnum.COUPON_DISCOUNT_AMOUNT.getCode().compareTo(amountType) == 0){
                        orderAmount.setAmount(totalDiscountAmount);
                    }
                }

            CalculateOrderAmountDTO calculateOrderAmount = new CalculateOrderAmountDTO();
                calculateOrderAmount.setOrderAmountDTOList(orderAmounts);
                calculateOrderAmount.setOrderAmountDetailDTOList(orderAmountDetails);

                return calculateOrderAmount;

        }

        return null;
    }

    /**
     * @description: 计算订单运费价格
     * @param regionId  区域Id
     * @param orderAmountMap 订单费用Map
     * @return 运费 价格
     * @author Long
     * @date: 2022/2/26 15:16
     */
    private BigDecimal calculateOrderShippingAmount(String regionId, Map<Integer, CalculateOrderAmountDTO.OrderAmountDTO> orderAmountMap) {
      // 运费
        BigDecimal shippingAmount = BigDecimal.ZERO;

        // 满 多少减 运费
        BigDecimal conditionAmount = BigDecimal.ZERO;
        // 查找 运费模板
        FreightTemplateDO freightTemplateDO = freightTemplateDao.getByRegionId(regionId);
        if (ObjectUtils.isNotEmpty(freightTemplateDO)){
            shippingAmount = freightTemplateDO.getShippingAmount();
            conditionAmount = freightTemplateDO.getConditionAmount();

        }

        // 订单金额
        BigDecimal originPayAmount = BigDecimal.ZERO;

        if (orderAmountMap.get(AmountTypeEnum.ORIGIN_PAY_AMOUNT.getCode()) != null){
            originPayAmount = orderAmountMap.get(AmountTypeEnum.ORIGIN_PAY_AMOUNT.getCode()).getAmount();
        }

        // 原价大于单金额指定则免运费
        if (originPayAmount.compareTo(conditionAmount) > 0){
            shippingAmount = BigDecimal.ZERO;
        }

        return shippingAmount;

    }

    /**
     * @description:  创建订单费用详情
     * @param orderId 订单Id
     * @param amountCode 费用类型 编码
     * @param  discountAmount 折扣费用
     * @param  realPayAmount 真实支付费用
     * @param orderItemRequest 订单条目请求 信息
     * @return  订单费用详情
     * @author Long
     * @date: 2022/2/26 14:10
     */
    private CalculateOrderAmountDTO.OrderAmountDetailDTO createOrderAmountDetailDTO(String orderId, Integer amountCode, BigDecimal discountAmount, BigDecimal  realPayAmount, CalculateOrderAmountRequest.OrderItemRequest orderItemRequest) {

        CalculateOrderAmountDTO.OrderAmountDetailDTO orderAmountDetailDTO = new CalculateOrderAmountDTO.OrderAmountDetailDTO();

        orderAmountDetailDTO.setOrderId(orderId);
        orderAmountDetailDTO.setProductType(orderItemRequest.getProductType());
        orderAmountDetailDTO.setSkuCode(orderItemRequest.getSkuCode());
        orderAmountDetailDTO.setSalePrice(orderItemRequest.getSalePrice());
        orderAmountDetailDTO.setSaleQuantity(orderItemRequest.getSaleQuantity());
        orderAmountDetailDTO.setAmountType(amountCode);
        if (AmountTypeEnum.ORIGIN_PAY_AMOUNT.getCode().compareTo(amountCode) == 0){
            BigDecimal originPayAmount = orderItemRequest.getSalePrice().multiply(new BigDecimal(orderItemRequest.getSaleQuantity()));
            orderAmountDetailDTO.setAmount(originPayAmount);
        }else if (AmountTypeEnum.COUPON_DISCOUNT_AMOUNT.getCode().compareTo(amountCode) == 0){
            orderAmountDetailDTO.setAmount(discountAmount);
        }else if (AmountTypeEnum.REAL_PAY_AMOUNT.getCode().compareTo(amountCode) == 0){
            orderAmountDetailDTO.setAmount(realPayAmount);
        }
        return orderAmountDetailDTO;
    }

    /** 
     * @description:  获取用户优惠券
     * @param userId
     * @param couponId
     * @return  com.taki.market.domain.entity.CouponDO
     * @author Long
     * @date: 2022/2/18 17:52
     */ 
    private CouponDO getCoupAchieve(String userId, String couponId) {

        CouponDO couponDO = couponDao.getUserCoupon(userId,couponId);

        if (ObjectUtils.isEmpty(couponDO)){
            throw new MarketBizException(MarketErrorCodeEnum.USER_COUPON_IS_NULL);
        }
        return couponDO;

    }

    private void checkCalculateOrderAmountRequest(CalculateOrderAmountRequest calculateOrderAmountRequest) {

        String  orderId = calculateOrderAmountRequest.getOrderId();
        ParamCheckUtil.checkStringNonEmpty(orderId);
        String userId = calculateOrderAmountRequest.getUserId();
        ParamCheckUtil.checkStringNonEmpty(userId);
        List<CalculateOrderAmountRequest.OrderItemRequest> orderItemRequests  = calculateOrderAmountRequest.getOrderItemRequests();
        ParamCheckUtil.checkCollectionNonEmpty(orderItemRequests);
         List<CalculateOrderAmountRequest.OrderAmountRequest> orderAmountRequests =    calculateOrderAmountRequest.getOrderAmountRequests();
         ParamCheckUtil.checkCollectionNonEmpty(orderAmountRequests);
    }

}
