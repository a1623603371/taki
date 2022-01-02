package com.taki.order.domin.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName GenOrderIdRequest
 * @Description 生成订单Id 请求
 * @Author Long
 * @Date 2022/1/2 19:41
 * @Version 1.0
 */
@Data
public class GenOrderIdRequest extends AbstractObject implements Serializable {


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
