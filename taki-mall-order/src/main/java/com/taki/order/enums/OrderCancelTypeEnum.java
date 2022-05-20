package com.taki.order.enums;

import com.taki.common.enums.OrderStatusEnum;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName OrderCancelTypeEnum
 * @Description 订单取消类型枚举
 * @Author Long
 * @Date 2022/1/12 10:07
 * @Version 1.0
 */
@Getter
public enum OrderCancelTypeEnum {
    USE_CANCELED(0, "用户手动取消"),
    TIMEOUT_CANCELED(1, "订单超时，取消"),
    CUSTOMER_CANCELED(2, "用户授权客服取消");

    OrderStatusEnum
    private Integer code;


    private String msg;

    OrderCancelTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }


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
