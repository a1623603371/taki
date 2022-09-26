package com.taki.market.enums;

import lombok.Getter;

import java.util.Map;

/**
 * @ClassName CouponTypeEnum
 * @Description 优惠券类型
 * @Author Long
 * @Date 2022/9/25 20:52
 * @Version 1.0
 */
@Getter
public enum CouponTypeEnum {
    /**
     * 满减
      */
REDUCTION(1,"满减"),
    /**
     * 折扣
     */
 DISCOUNT(2,"折扣")   ;


    CouponTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;

    private String msg;


    public static CouponTypeEnum getByCode(Integer code){

        for (CouponTypeEnum value : CouponTypeEnum.values()) {
            if (code.equals(value.getCode())){
                return value;
            }
        }
        return null;
    }

}
