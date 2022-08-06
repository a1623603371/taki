package com.taki.common.enums;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName OrderOperateTypeEnum
 * @Description 订单操作类型 枚举
 * @Author Long
 * @Date 2022/1/3 16:41
 * @Version 1.0
 */
@Getter
public enum OrderOperateTypeEnum {


    NEW_ORDER(10,"新建订单"),
    MANUAL_CANCEL_ORDER(20,"手工取消订单"),
    AUTO_CANCEL_ORDER(30,"超时未支付自动取消订单"),
    PAID_ORDER(40,"完成订单支付"),
    PUSH_ORDER_FULFILL(50,"推送订单至履约"),
    ORDER_OUT_STOCK(60,"订单出库"),
    ORDER_DELIVER0D(70,"订单已配送"),
    ORDER_SIGNED(80,"订单已签收")
    ;

private     Integer code;

private String smg;

    OrderOperateTypeEnum(Integer code, String smg) {
        this.code = code;
        this.smg = smg;
    }

    /**
     * 生成 map
     * @return
     */
    public  static Map<Integer,String> toMap(){
        Map<Integer,String> map = new HashMap<>();
        for (PayTypeEnum element : PayTypeEnum.values()) {
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
    public static PayTypeEnum getByCode (Integer code){

        for (PayTypeEnum element : PayTypeEnum.values()) {

            if (element.getCode().equals(code)){
                return element;
            }


        }
        return null;
    }
}
