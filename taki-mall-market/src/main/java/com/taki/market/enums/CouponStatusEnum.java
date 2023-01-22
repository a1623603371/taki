package com.taki.market.enums;

import com.taki.common.exception.BaseErrorCodeEnum;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName CouponStatusEnum
 * @Description TODO
 * @Author Long
 * @Date 2022/2/18 15:25
 * @Version 1.0
 */
public enum CouponStatusEnum  {
        NORMAL(1,"正常"),
        USED(2,"已使用"),
        EXPIRED(3,"已过期");


    CouponStatusEnum(Integer code, String msg) {
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


    public static Map<Integer,String> toMap(){

    Map<Integer,String> map = new HashMap<>();

    return CouponStatusEnum.toMap();
    }

    public static CouponStatusEnum getByCode(Integer code){

        for (CouponStatusEnum value : CouponStatusEnum.values()) {
            if (value.Code.equals(code))
                return value;
        }

        return null;
    }
}
