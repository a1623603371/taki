package com.taki.fulfill.exection;

import com.taki.common.exception.BaseErrorCodeEnum;

/**
 * @ClassName FulfillErrorCodeEnum
 * @Description 履约错误 类型 枚举
 * @Author Long
 * @Date 2022/5/15 18:25
 * @Version 1.0
 */
public enum FulfillErrorCodeEnum implements BaseErrorCodeEnum {
    FULFILL_ID_GEN_ERROR(107001,"履约单ID 生成异常"),
    WMS_IS_ERROR(107002,"调用WMS系统异常"),
    TMS_IS_ERROR(107003,"调用TMS系统异常"),
    ORDER_FULFILL_IS_ERROR(107004,"履约流程执行异常"),
    SEND_MQ_FAILED(107005,"发送MQ消息失败"),
    ORDER_FULFILL_ERROR(107006,"订单履约错误")
    ;

    private Integer errorCode;

    private String errorMsg;

    FulfillErrorCodeEnum(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public Integer getErrorCode() {
        return errorCode;
    }

    @Override
    public String getErrorMsg() {
        return errorMsg;
    }
}
