package com.taki.inventory.exception;

import com.taki.common.exception.BaseErrorCodeEnum;

/**
 * @ClassName InventoryErrorCodeEnum
 * @Description 库存错误码
 * @Author Long
 * @Date 2022/2/16 17:24
 * @Version 1.0
 */
public enum InventoryErrorCodeEnum implements BaseErrorCodeEnum {
    PRODUCT_SKU_STOCK_ERROR(100001,"商品库存记录不存在"),
    LOCK_PRODUCT_SKU_STOCK_ERROR(100002,"锁定商品库存失败"),
    RELEASE_PRODUCT_SKU_STOCK_ERROR(100003,"释放商品库存失败"),
    CONSUME_MQ_FAILED(100004,"消费MQ消息失败");


    /**
     * 错误码
     */
    private Integer errorCode;
    /**
     * 错误信息
     */
    private String errorMsg;

    InventoryErrorCodeEnum(Integer errorCode, String errorMsg) {
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
