package com.taki.order.manager;

/**
 * @ClassName OrderNoManager
 * @Description 订单id 生成 组件
 * @Author Long
 * @Date 2022/1/2 21:20
 * @Version 1.0
 */
public interface OrderNoManager {


    /***
     * @description: 生成 订单Id
     * @param orderNoType 订单id 类型
     * @param userId 用户id
     * @return  订单id
     * @author Long
     * @date: 2022/1/2 21:21
     */
    String genOrderId(Integer orderNoType,String userId);
}
