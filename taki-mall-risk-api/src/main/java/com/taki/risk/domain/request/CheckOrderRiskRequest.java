package com.taki.risk.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName CheckOrderRiskRequest
 * @Description 订单 风控 检查请求
 * @Author Long
 * @Date 2022/1/4 9:57
 *@Version 1.0
 *
 */
@Data
public class CheckOrderRiskRequest extends AbstractObject implements Serializable {


    private static final long serialVersionUID = 438278059275113452L;

    /**
     *业务标识
     */
    private Integer businessIdentifier;
    /**
     * 订单id
     */
    private String orderId;

    /**
     * 用户Id
     */
    private String userId;

    /**
     *卖家Id
     */
    private String sellerId;
    /**
     *  客户端IP
     */
    private String clientIp;

    /**
     *设备标识
     */
    private String deviceId;
}
