package com.taki.fulfill.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @ClassName ReceiveFulFillRequest
 * @Description 接收订单履约请求
 * @Author Long
 * @Date 2022/3/4 10:53
 * @Version 1.0
 */
@Data
@Builder
public class ReceiveFulFillRequest  extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -3267101977830300294L;

    /**
     * 订单号
     */
    private String orderId;

    /**
     * 商家id
     */
    private String sellerId;

    /**
     * 用户id
     */
    private String userId;

    /**
     * 配送类型，默认是自配送
     */
    private Integer deliveryType;

    /**
     * 收货人姓名
     */
    private String receiverName;

    /**
     * 收货人电话
     */
    private String receiverPhone;

    /**
     * 省
     */
    private String receiverProvince;

    /**
     * 市
     */
    private String receiverCity;

    /**
     * 区
     */
    private String receiverArea;

    /**
     * 街道地址
     */
    private String receiverStreetAddress;

    /**
     * 详细地址
     */
    private String receiverDetailAddress;

    /**
     * 经度 六位小数点
     */
    private BigDecimal receiverLon;

    /**
     * 纬度 六位小数点
     */
    private BigDecimal receiverLat;

    /**
     * 商家备注
     */
    private String shopRemark;

    /**
     * 用户备注
     */
    private String userRemark;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     *  付款总金额
     */
    private BigDecimal payAmount;

    /**
     * 交易总金额
     */
    private BigDecimal totalAmount;

    /**
     * 运费
     */
    private BigDecimal deliveryAmount;


    /**
     * 模拟 履约服务异常
     */
    private String fulfillException;

    /**
     * 模拟 wms服务异常
     */
    private String wmsException;

    /**
     * 模拟 tms服务异常
     */
    private String tmsException;

    /**
     * 订单商品明细
     */
    private List<ReceiveOrderItemRequest> receiveOrderItems;

    @Tolerate
    public ReceiveFulFillRequest() {
    }


}
