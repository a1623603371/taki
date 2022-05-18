package com.taki.order.enums;

import lombok.Getter;

/**
 * @ClassName AfterSaleStatusChannelEnum
 * @Description 售后单状态 变化枚举
 * @Author Long
 * @Date 2022/4/5 18:15
 * @Version 1.0
 */
@Getter
public enum AfterSaleStatusChannelEnum {
        AFTER_SALE_REVOKE(AfterSaleStatusEnum.COMMITED,AfterSaleStatusEnum.REVOKE,"销售单撤销"),
        AFTER_SALE_REFUNDING(AfterSaleStatusEnum.REVIEW_PASS,AfterSaleStatusEnum.REFUNDING,"售后退款中"),
        AFTER_SALE_CUSTOMER_AUDIT_PASS(AfterSaleStatusEnum.COMMITED,AfterSaleStatusEnum.REVIEW_PASS,"客服审核通过"),
        AFTER_SALE_CUSTOMER_AUDIT_REJECT(AfterSaleStatusEnum.COMMITED,AfterSaleStatusEnum.REVIEW_REJECTED,"客服审核拒绝通过"),
        AFTER_SALE_PAYMENT_CALLBACK_PASS(AfterSaleStatusEnum.REFUNDING,AfterSaleStatusEnum.REFUNDED,"三方支付系统退款回调成功"),
        AFTER_SALE_PAYMENT_CALLBACK_FAILED(AfterSaleStatusEnum.REFUNDING,AfterSaleStatusEnum.FAILED,"三方支付系统退款回调失败")





    ;



    AfterSaleStatusChannelEnum(AfterSaleStatusEnum preStatus, AfterSaleStatusEnum currentStatus, String operateRemark) {
        this.preStatus = preStatus;
        this.currentStatus = currentStatus;
        this.operateRemark = operateRemark;
    }

    private AfterSaleStatusEnum preStatus;

    private AfterSaleStatusEnum currentStatus;

    private String operateRemark;


    public static AfterSaleStatusChannelEnum getBy(int perStatus,int currentStatus){
        for (AfterSaleStatusChannelEnum channelEnum : AfterSaleStatusChannelEnum.values()) {
            if (perStatus == channelEnum.preStatus.getCode() && currentStatus == channelEnum.currentStatus.getCode()){
                return channelEnum;
            }
        }
        return null;
    }

}
