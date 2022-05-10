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
    PRODUCT_SKU_STOCK_NOT_FOUND_ERROR(100001,"商品库存记录不存在"),
    LOCK_PRODUCT_SKU_EXISTED_ERROR(100002,"锁定商品库存失败"),
    DEDUCT_PRODUCT_SKU_STOCK_ERROR(100003,"扣减库存失败"),
    RELEASE_PRODUCT_SKU_STOCK_ERROR(100004,"释放商品库存失败"),
    CONSUME_MQ_FAILED(100005,"消费MQ消息失败"),
    SKU_CODE_IS_EMPTY(100006,"skuCode不能为空"),
    SALE_STOCK_QUANTITY_IS_EMPTY(100007,"销售库存数量不能为空"),
    SALE_STOCK_QUANTITY_CANNOT_BE_NEGATIVE_NUMBER(100008,"销售库存增量不能小于0"),
    ADD_PRODUCT_SKU_STOCK_ERROR(100009,"添加商品库存异常"),
    SALE_STOCK_INCREMENTAL_QUANTITY_IS_EMPTY(100010,"销售库存增量不能为空"),
    SALE_STOCK_INCREMENTAL_QUANTITY_CANNOT_BE_ZERO(100011,"销售库存增量不能为0"),
    MODIFY_PRODUCT_SKU_STOCK_ERROR(100012,"调整商品库存异常")
    ;



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
