package com.taki.inventory.enums;

/**
 * @ClassName StockLogStatusEnum
 * @Description 库存扣减日志状态
 * @Author Long
 * @Date 2022/6/20 16:08
 * @Version 1.0
 */
public enum StockLogStatusEnum {

    DEDUCTED(1,"已扣减"),
    RELEASE(2,"已释放");



    private Integer code;

    private String msg;

    StockLogStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
