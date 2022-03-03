package com.taki.order.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @ClassName OrderTypeEnum
 * @Description TODO
 * @Author Long
 * @Date 2022/1/3 15:26
 * @Version 1.0
 */
@Getter
public enum OrderTypeEnum {

    NORMAL(1,"一般订单"),
    UNKNOWN(255,"其他");


    private Integer code;


    private String msg;

    OrderTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 生成 map
     * @return
     */
    public  static Map<Integer,String> toMap(){
        Map<Integer,String> map = new HashMap<>();
        for (OrderTypeEnum element : OrderTypeEnum.values()) {
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
    public static OrderTypeEnum getByCode (Integer code){

        for (OrderTypeEnum element : OrderTypeEnum.values()) {

            if (element.equals(code)){
                return element;
            }


        }
        return null;
    }

    public static Set<Integer> allowableValues(){
        Set<Integer> allowableValues = new HashSet<>(values().length);

        for (BusinessIdentifierEnum businessIdentifierEnum : BusinessIdentifierEnum.values()) {

            allowableValues.add(businessIdentifierEnum.getCode());
        }
        return allowableValues;
    }
}
