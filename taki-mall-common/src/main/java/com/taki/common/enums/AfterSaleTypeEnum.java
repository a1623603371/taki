package com.taki.common.enums;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName AfterSaleTypeEnum
 * @Description 售后类型枚举
 * @Author Long
 * @Date 2022/3/11 10:40
 * @Version 1.0
 */
public enum AfterSaleTypeEnum {

    RETURN_MONEY(1, "退款"),
    RETURN_GOODS(2, "退货");

    private Integer code;

    private String msg;

    AfterSaleTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public static Map<Integer, String> toMap() {
        return AfterSaleTypeEnum.toMap();
    }


    public static AfterSaleTypeEnum getByCode(Integer code) {
        for (AfterSaleTypeEnum value : AfterSaleTypeEnum.values()) {

            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }

    public static Set<Integer> allowableValues(){
        Set<Integer> allowableValues = new HashSet<>(values().length);

        for (Integer allowableValue : allowableValues) {
            allowableValues.add(allowableValue);
        }
        return allowableValues;
    }
}