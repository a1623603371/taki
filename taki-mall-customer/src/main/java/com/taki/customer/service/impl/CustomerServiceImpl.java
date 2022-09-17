package com.taki.customer.service.impl;

import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.redis.RedisLock;
import com.taki.common.utli.ParamCheckUtil;
import com.taki.customer.converter.CustomerConverter;
import com.taki.customer.dao.CustomerReceivesAfterSaleInfoDAO;
import com.taki.customer.domain.enetity.CustomerReceivesAfterSaleInfoDO;
import com.taki.customer.domain.request.CustomReviewReturnGoodsRequest;
import com.taki.customer.domain.request.CustomerReceiveAfterSaleRequest;
import com.taki.customer.exception.CustomerBizException;
import com.taki.customer.exception.CustomerErrorCodeEnum;
import com.taki.customer.remote.AfterSaleRemote;
import com.taki.customer.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ClassName CustomerServiceImpl
 * @Description TODO
 * @Author Long
 * @Date 2022/7/30 20:31
 * @Version 1.0
 */
@Service
public class CustomerServiceImpl implements CustomerService {


    @Autowired
    private CustomerReceivesAfterSaleInfoDAO customerReceivesAfterSaleInfoDAO;

    @Autowired
    private RedisLock redisLock;

    @Autowired
    private AfterSaleRemote afterSaleRemote;

    @Autowired
    private CustomerConverter customerConverter;


    @Override
    public Boolean receiveAfterSale(CustomerReceiveAfterSaleRequest customerReceivesAfterSaleRequest) {
        // 1.效验 入参
        checkCustomerReceiveAfterSaleRequest(customerReceivesAfterSaleRequest);
        //2 分布式锁
        Long afterSaleId = customerReceivesAfterSaleRequest.getAfterSaleId();
        String key = RedisLockKeyConstants.REFUND_KEY + afterSaleId;

        boolean lock = redisLock.tryLock(key);

        if (!lock){
            throw new CustomerBizException(CustomerErrorCodeEnum.PROCESS_RECEIVE_AFTER_SALE_REPEAT);
        }

        try {
            Long   afterSaleRefundId = afterSaleRemote.customerFindAfterSaleRefundInfo(customerReceivesAfterSaleRequest);
            customerReceivesAfterSaleRequest.setAfterSaleRefundId(afterSaleRefundId);

            CustomerReceivesAfterSaleInfoDO customerReceivesAfterSaleInfo = customerConverter.converterCustomerReceivesAfterSaleInfoDO(customerReceivesAfterSaleRequest);

           Boolean result =   customerReceivesAfterSaleInfoDAO.save(customerReceivesAfterSaleInfo);

            return result;
        }catch (Exception e){
            throw new CustomerBizException(CustomerErrorCodeEnum.SAVE_AFTER_SALE_INFO_FAILED);
        }finally {
            redisLock.unLock(key);
        }


    }


    /**
     * @description:  检查售后单请求 参数
     * @param request
     * @return  void
     * @author Long
     * @date: 2022/7/30 20:37
     */
    private void checkCustomerReceiveAfterSaleRequest(CustomerReceiveAfterSaleRequest request) {

        ParamCheckUtil.checkObjectNonNull(request.getAfterSaleId(), CustomerErrorCodeEnum.AFTER_SALE_ID_IS_NULL);
        ParamCheckUtil.checkStringNonEmpty(request.getUserId(),CustomerErrorCodeEnum.USER_ID_IS_NULL);
        ParamCheckUtil.checkStringNonEmpty(request.getOrderId(),CustomerErrorCodeEnum.ORDER_ID_IS_NULL);
        ParamCheckUtil.checkObjectNonNull(request.getAfterSaleType(),CustomerErrorCodeEnum.AFTER_SALE_TYPE_IS_NULL);
        ParamCheckUtil.checkObjectNonNull(request.getApplyRefundAmount(),CustomerErrorCodeEnum.APPLY_REFUND_AMOUNT_IS_NULL);
        ParamCheckUtil.checkObjectNonNull(request.getReturnGoodsAmount(),CustomerErrorCodeEnum.REFUND_GOOD_AMOUNT_IS_NULL);

    }

    @Override
    public Boolean customerAudit(CustomReviewReturnGoodsRequest customReviewReturnGoodsRequest) {
        return afterSaleRemote.receiveCustomerAuditResult(customReviewReturnGoodsRequest);
    }
}
