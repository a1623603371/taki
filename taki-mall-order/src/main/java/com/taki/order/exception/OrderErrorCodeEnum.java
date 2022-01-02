package com.taki.order.exception;

import com.taki.common.exception.BaseErrorCodeEnum;

/**
 * @ClassName OrderErrorCodeEnum
 * @Description 订单错误编码枚举
 * @Author Long
 * @Date 2022/1/2 22:20
 * @Version 1.0
 */
public enum OrderErrorCodeEnum implements BaseErrorCodeEnum{
    /**
     * 正向订单错误吗 开头 105
     */
     USER_ID_IS_NULL(105001,"用户ID不能为空"),
     ORDER_NO_TYPE_ERROR(105002,"订单类型错误"),
     CREATE_ORDER_REQUEST_ERROR(105003,"创建订单请求错误"),
     ORDER_ID_IS_NULL(105004,"订单号不能为空"),
    BUSINESS_IDENTIFIER_IS_NULL(105005,"业务标识不能为空"),
    BUSINESS_IDENTIFIER_ERROR(105006,"业务标识错误"),
    ORDER_TYPE_IS_NULL(105007,"订单类型不能为空"),
    ORDER_TYPE_ERROR(105008,"订单类型错误"),
    SELLER_ID_IS_NULL(105009,"卖家ID不能为空"),
    USER_ADDRESS_ERROR(105010,"地址信息错误"),
    DELIVERY_TYPE_IS_NULL(105011,"配送类型不能为空");
























    ;



    OrderErrorCodeEnum(Integer errorCode, String errorMsg) {
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
