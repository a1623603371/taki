package com.taki.pay.exception;

import com.taki.common.exception.BaseErrorCodeEnum;

/**
 * @ClassName PayErrorCodeEnum
 * @Description TODO
 * @Author Long
 * @Date 2022/5/18 15:55
 * @Version 1.0
 */
public enum PayErrorCodeEnum implements BaseErrorCodeEnum {
   PAY_REFUND_FAILED(600001,"支付退款接口调用失败") ;


    PayErrorCodeEnum(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    private Integer errorCode;

    private String errorMsg;
    @Override
    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
}
