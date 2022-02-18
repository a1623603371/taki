package com.taki.product.exception;

import com.taki.common.exception.BaseErrorCodeEnum;

/**
 * @ClassName ProductErrorCodeEnum
 * @Description TODO
 * @Author Long
 * @Date 2022/2/17 16:16
 * @Version 1.0
 */
public enum ProductErrorCodeEnum implements BaseErrorCodeEnum {
    SKU_CODE_IS_NULL(200001,"sku 编码不能为空");


    private Integer errorCode;

    private String errorMsg;

    ProductErrorCodeEnum(Integer errorCode, String errorMsg) {
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
