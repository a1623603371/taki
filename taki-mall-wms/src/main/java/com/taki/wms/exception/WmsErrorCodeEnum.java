package com.taki.wms.exception;

import com.taki.common.exception.BaseErrorCodeEnum;

/**
 * @ClassName WmsErrorCodeEnum
 * @Description TODO
 * @Author Long
 * @Date 2022/5/17 14:06
 * @Version 1.0
 */
public enum WmsErrorCodeEnum implements BaseErrorCodeEnum {
   DELIVERY_ORDER_ID_GEN_ERROR(108001,"出库单ID生成异常"),
    TMS_IS_ERROR(108002,"tms系统异常");

    WmsErrorCodeEnum(Integer errorCode, String errorMsg) {
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
