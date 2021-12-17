package com.taki.user.enums;

/**
 * @ClassName RegisterEnum
 * @Description TODO
 * @Author Long
 * @Date 2021/12/17 17:19
 * @Version 1.0
 */
public enum RegisterEnum   {

    PHONE("phone"),
    EMAIL("email");


    private String value;

    RegisterEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
