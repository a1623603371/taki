package com.taki.order.enums;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName OrderNoTypeEnum
 * @Description 订单号类型枚举
 * @Author Long
 * @Date 2022/3/11 10:14
 * @Version 1.0
 */
public enum OrderNoTypeEnum {

    SALE_ORDER(10,"正向订单"),
    AFTER_SALE(20,"逆向订单");

    private Integer code;

    private String msg;

    OrderNoTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Map<Integer,String> toMap(){

        Map<Integer,String> map = new HashMap<>(16);

        for (OrderNoTypeEnum value : OrderNoTypeEnum.values()) {
            map.put(value.getCode(),value.getMsg());
            
        }

        return map;
    }


    public static OrderNoTypeEnum getByCode(Integer code){
        for (OrderNoTypeEnum value : OrderNoTypeEnum.values()) {
           if (code.equals(value.getCode())){
                return value;
           }

        }
        return null;
    }

    public Integer getCode() {
        return code;
    }



    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
