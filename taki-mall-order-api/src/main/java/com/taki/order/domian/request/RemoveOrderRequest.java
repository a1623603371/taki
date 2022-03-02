package com.taki.order.domian.request;

import lombok.Data;

import java.util.Set;

/**
 * @ClassName RemoveOrderRequest
 * @Description 删除订单请求参数
 * @Author Long
 * @Date 2022/2/26 17:42
 * @Version 1.0
 */
@Data
public class RemoveOrderRequest {

    private Set<String> orderIds;
}
