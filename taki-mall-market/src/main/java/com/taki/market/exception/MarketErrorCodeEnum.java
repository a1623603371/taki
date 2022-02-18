package com.taki.market.exception;

import com.taki.common.exception.BaseErrorCodeEnum;

/**
 * @ClassName MarketErrorCodeEnum
 * @Description 异常错误码
 * @Author Long
 * @Date 2022/2/18 14:34
 * @Version 1.0
 */
public enum MarketErrorCodeEnum implements BaseErrorCodeEnum {
    USER_COUPON_IS_NULL(300001,"优惠券记录不存在"),
    USER_COUPON_IS_USED(300002,"优惠券已经被使用"),
    USER_COUPON_CONFIG_IS_NULL(300003,"优惠券活动设置记录不存在"),
    SEND_MQ_FAILED(300004,"发送MQ消息失败"),
    CONSUME_MQ_FAILED(300005,"消费MQ 消息失败");




    private Integer errorCode;


    private String errorMsg;

    MarketErrorCodeEnum(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public Integer getErrorCode() {
        return null;
    }

    @Override
    public String getErrorMsg() {
        return null;
    }
}
