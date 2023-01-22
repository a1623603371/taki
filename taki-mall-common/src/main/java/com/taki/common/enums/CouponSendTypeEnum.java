package com.taki.common.enums;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName CouponSendTypeEnum
 * @Description TODO
 * @Author Long
 * @Date 2022/10/4 12:04
 * @Version 1.0
 */
public enum CouponSendTypeEnum {

    SELF_RECEIVE(1,"仅自己领取"),
    PLATFORM_SEND(2,"系统发放")
    ;


    private Integer code;

    private String msg;

    CouponSendTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }


    public static Map<Integer,String> toMap() {

        Map<Integer,String> map = new HashMap<>(16);

        for (CouponSendTypeEnum enums : CouponSendTypeEnum.values()) {

            map.put(enums.getCode(),enums.getMsg());
            
        }

        return map;
    }

    public static CouponSendTypeEnum getByCode(Integer code){

        for (CouponSendTypeEnum value : CouponSendTypeEnum.values()) {

            if (value.code.equals(code)){
                return value;
            }
            
        }

        return null;
        
    }

    public static Set<Integer> allowableValues(){
        Set<Integer> allowableValues = new HashSet<>(values().length);

        for (CouponSendTypeEnum value : values()) {
            allowableValues.add(value.getCode());
        }

        return allowableValues;
    }
}
