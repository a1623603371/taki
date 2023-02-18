package com.taki.careerplan.cookbook.exception;

import com.taki.common.exception.BaseErrorCodeEnum;

/**
 * @ClassName AddressErrorCodeError
 * @Description TODO
 * @Author Long
 * @Date 2022/7/31 16:40
 * @Version 1.0
 */
public enum CookBookErrorCodeError implements BaseErrorCodeEnum {
    PARAM_CANNOT_ALL_EMPTY(150001,"查询入参不能全部为空") ;


    private Integer errorCode;

    private String errorMsg;


    CookBookErrorCodeError(Integer errorCode, String errorMsg) {
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
