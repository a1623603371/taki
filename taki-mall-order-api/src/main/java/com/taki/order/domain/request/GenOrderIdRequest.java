package com.taki.order.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
/**
 * @ClassName CreateOrderRequest
 * @Description 生成订单Id 请求
 * * @Author Long
 * @Date 2022/1/3 0:16
 * @Version 1.0
 */
@Data
public class  GenOrderIdRequest  implements Serializable {


    private static final long serialVersionUID = 8463280326250994794L;


    /**
     * 业务线标识
     */
    private Integer businessIdentifier;

    /**
     * 用户id
     */
    private String userId;
}
