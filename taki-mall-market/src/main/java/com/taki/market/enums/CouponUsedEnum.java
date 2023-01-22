package com.taki.market.enums;

/**
 * @ClassName CouponUsedEnum
 * @Description TODO
 * @Author Long
 * @Date 2022/10/4 21:10
 * @Version 1.0
 */
public enum CouponUsedEnum {

    UN_USED(0,"未使用"),
    USED(1,"已使用");


    CouponUsedEnum(Integer code, String msg) {
        Code = code;
        Msg = msg;
    }

    private Integer Code;

    private String Msg;

    public Integer getCode() {
        return Code;
    }

    public String getMsg() {
        return Msg;
    }
}
