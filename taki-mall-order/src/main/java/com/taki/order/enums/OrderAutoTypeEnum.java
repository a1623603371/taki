package com.taki.order.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName OrderAutoTypeEnum
 * @Description 订单id 生成方向类型枚举
 * @Author Long
 * @Date 2022/1/2 21:59
 * @Version 1.0
 */
@Getter
public enum OrderAutoTypeEnum {

    SALE_ORDER(10,"正向订单号"),
     AFTER_ORDER(20,"反向订单号");


    OrderAutoTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Integer code;

    private String msg;

    /**
     * 生成 map
     * @return
     */
    public  static Map<Integer,String> toMap(){
    Map<Integer,String> map = new HashMap<>();
        for (OrderAutoTypeEnum element : OrderAutoTypeEnum.values()) {
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
    public static OrderAutoTypeEnum getByCode (Integer code){

        for (OrderAutoTypeEnum element : OrderAutoTypeEnum.values()) {

            if (element.equals(code)){
                return element;
            }


        }
        return null;
    }
}
