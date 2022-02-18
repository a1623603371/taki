package com.taki.market.service.impl;

import cn.hutool.db.sql.Order;
import com.taki.common.utlis.ParamCheckUtil;
import com.taki.market.dao.CouponDao;
import com.taki.market.dao.FreightTemplateDao;
import com.taki.market.domain.dto.CalculateOrderAmountDTO;
import com.taki.market.domain.entity.CouponDO;
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
import java.util.List;

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
        }





        return null;
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
