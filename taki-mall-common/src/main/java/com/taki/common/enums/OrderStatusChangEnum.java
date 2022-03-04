package com.taki.common.enums;

import lombok.Getter;

/**
 * @ClassName OrderStatusChangEnum
 * @Description 订单转换变化枚举
 * @Author Long
 * @Date 2022/3/4 11:02
 * @Version 1.0
 */
@Getter
public enum OrderStatusChangEnum {

    /**
     * 订单已履约
     */
    ORDER_FULFILLED(OrderStatusEnum.PAID,OrderStatusEnum.FULFILL,OrderOperateTypeEnum.PUSH_ORDER_FULFILL),

    /**
     * 订单已出库
     */
    ORDER_OUT_STOCKED(OrderStatusEnum.FULFILL,OrderStatusEnum.OUT_STOCK,OrderOperateTypeEnum.ORDER_OUT_STOCK),

    /**
     * 订单已配送
     */
    ORDER_DELIVERED(OrderStatusEnum.OUT_STOCK,OrderStatusEnum.DELIVERY,OrderOperateTypeEnum.ORDER_DELIVER0D),

    /**
     * 订单已签收
     */
    ORDER_SIGNED(OrderStatusEnum.DELIVERY,OrderStatusEnum.SIGNED,OrderOperateTypeEnum.ORDER_SIGNED);
    ;





    private OrderStatusEnum preStatus;

    private OrderStatusEnum currentStatus;

    private OrderOperateTypeEnum operateType;


    OrderStatusChangEnum(OrderStatusEnum preStatus, OrderStatusEnum currentStatus, OrderOperateTypeEnum operateType) {
        this.preStatus = preStatus;
        this.currentStatus = currentStatus;
        this.operateType = operateType;
    }


    public static OrderStatusChangEnum getBy(int  preStatus,int currentStatus){

        for (OrderStatusChangEnum element : OrderStatusChangEnum.values()) {
                if (preStatus == element.preStatus.getCode() && currentStatus == element.currentStatus.getCode()){
                    return element;

                }
        }
        return null;
    }

    public static OrderStatusChangEnum getBy(int  operateType){
        for (OrderStatusChangEnum element : OrderStatusChangEnum.values()) {
            
            if (operateType == element.operateType.getCode()){
                return element;
            }
        }
        return  null;
    }
}
