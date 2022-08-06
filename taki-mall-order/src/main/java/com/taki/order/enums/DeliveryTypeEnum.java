package com.taki.order.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName DeliveryTypeEnum
 * @Description 地址类型
 * @Author Long
 * @Date 2022/1/3 15:33
 * @Version 1.0
 */
@Getter
public enum DeliveryTypeEnum {
    NULL(0,"无配送方式"),
    SELF(1,"自主配送");

    private  Integer code;

    private String msg;

     DeliveryTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 生成 map
     * @return
     */
    public  static Map<Integer,String> toMap(){
        Map<Integer,String> map = new HashMap<>();
        for (DeliveryTypeEnum element : DeliveryTypeEnum.values()) {
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
    public static DeliveryTypeEnum getByCode (Integer code){

        for (DeliveryTypeEnum element : DeliveryTypeEnum.values()) {

            if (element.getCode().equals(code)){
                return element;
            }


        }
        return null;
    }
}
