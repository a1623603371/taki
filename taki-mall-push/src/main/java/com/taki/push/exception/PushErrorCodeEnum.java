package com.taki.push.exception;

import com.taki.common.exception.BaseErrorCodeEnum;

/**
 * @ClassName ProductErrorCodeEnum
 * @Description TODO
 * @Author Long
 * @Date 2022/2/17 16:16
 * @Version 1.0
 */
public enum PushErrorCodeEnum implements BaseErrorCodeEnum {
   ;


    private Integer errorCode;

    private String errorMsg;

    PushErrorCodeEnum(Integer errorCode, String errorMsg) {
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
