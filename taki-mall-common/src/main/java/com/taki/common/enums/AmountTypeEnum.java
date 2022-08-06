package com.taki.common.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName AmountTypeEnum
 * @Description 金额类型枚举
 * @Author Long
 * @Date 2022/1/3 16:41
 * @Version 1.0
 */
@Getter
public enum AmountTypeEnum {
    ORIGIN_PAY_AMOUNT(10,"订单支付原价"),
    COUPON_DISCOUNT_AMOUNT(20,"优惠券抵扣金额"),
    SHIPPING_AMOUNT(30,"运费"),
    BOX_AMOUNT(40,"包装费"),
    REAL_PAY_AMOUNT(50,"实付金额"),
    OTHER_AMOUNT(60,"其他类型");
    private Integer code;

    private String  msg;

    AmountTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


    /**
     * 生成 map
     * @return
     */
    public  static Map<Integer,String> toMap(){
        Map<Integer,String> map = new HashMap<>();
        for (AmountTypeEnum element : AmountTypeEnum.values()) {
            map.put(element.getCode(), element.getMsg());
        }

        return map;
    }

    /**
     * @description: 根据code放回订单号 方向
     * @param code 订单方向
     * @return  订单号类型
     * @author Long
     * @date: 2022/1/2 22:12
     */
    public static AmountTypeEnum getByCode (Integer code){

        for (AmountTypeEnum element : AmountTypeEnum.values()) {

            if (element.getCode().equals(code)){
                return element;
            }


        }
        return null;
    }
}
