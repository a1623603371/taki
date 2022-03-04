package com.taki.common.enums;

import com.google.common.collect.Lists;
import lombok.Getter;

import java.util.*;

/**
 * @ClassName OrderStatusEnum
 * @Description 订单状态 枚举
 * @Author Long
 * @Date 2022/1/6 11:35
 * @Version 1.0
 */
@Getter
public enum OrderStatusEnum {

    NULL(0,"未知"),
    CREATED(10,"已创建"),
    PAID(20,"已支付"),
    FULFILL(30,"以履约"),
    OUT_STOCK(40,"出库"),
    DELIVERY(50,"配送中"),
    SIGNED(60,"已签收"),
    CANCELED(70,"已取消"),
    REFUSED(100,"已拒收"),
    INVALID(127,"无效订单");


    private Integer code;


    private String  msg;

    OrderStatusEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Map<Integer,String> toMap(){
       return OrderStatusEnum.toMap();
    }

    public static OrderStatusEnum getByCode(Integer code){

        for (OrderStatusEnum orderStatusEnum : OrderStatusEnum.values()) {

            if (orderStatusEnum.getCode().compareTo(code) == 0){
                return orderStatusEnum;
            }
        }
        return null;

    }
    /*** 
     * @description: 未出库订单状态
     * @param 
     * @return  java.util.List<java.lang.Integer>
     * @author Long
     * @date: 2022/1/6 11:56
     */ 
    public static List<Integer> unOutStockStatus(){
        return Lists.newArrayList(CREATED.code,PAID.code,FULFILL.code);
    }

    /** 
     * @description: 未支付的订单状态
     * @param 
     * @return  java.util.List<java.lang.Integer>
     * @author Long
     * @date: 2022/1/6 11:57
     */ 
    public static List<Integer> unPaidStatus(){
        return Lists.newArrayList(CREATED.code);
    }

    /** 
     * @description: 效应状态
     * @param
     * @return  java.util.Set<java.lang.Integer>
     * @author Long
     * @date: 2022/1/6 11:58
     */ 
    public static Set<Integer> validStatus(){
        Set<Integer> validStatus = allowableValues();
        validStatus.remove(INVALID.code);
        return validStatus;
    }

    /*** 
     * @description: 有效订单状态
     * @param 
     * @return
     * @author Long
     * @date: 2022/1/6 11:59
     */ 
    public static Set<Integer> allowableValues() {
        Set<Integer> allowableValue = new HashSet<>(values().length);

        for (OrderStatusEnum orderStatusEnum : values()) {
            allowableValue.add(orderStatusEnum.getCode());
        }
        return allowableValue;
    }
    /**
     * 可以发起缺品的状态(支付之后，配送之前)
     * @return
     */
    public static Set<Integer> canLack() {
        Set<Integer> validStatus = allowableValues();
        validStatus.remove(FULFILL.code);
        validStatus.remove(OUT_STOCK.code);
        return validStatus;
    }


    public static List<Integer> canRemoveStatus() {

        return Lists.newArrayList(SIGNED.code, CANCELED.code, REFUSED.code, INVALID.code);

    }
}
