package com.taki.order.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.util.Set;

/**
 * @ClassName LackRequest
 * @Description TODO
 * @Author Long
 * @Date 2022/3/9 14:44
 * @Version 1.0
 */
@Data
public class LackRequest extends AbstractObject implements Serializable {


    private static final long serialVersionUID = 7829487296101589229L;


    /**
     * 订单Id
     */
    private String orderId;

    /**
     * 用户Id
     */
    private String userId;

    /**
     * 具体缺品项
     */
    private Set<LackItemRequest> lackItems;

}
