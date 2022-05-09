package com.taki.order.manager.impl;

import com.taki.common.exception.ServiceException;
import com.taki.common.utlis.DateFormatUtils;
import com.taki.common.utlis.NumberUtils;
import com.taki.order.dao.OrderAutoNoDao;

import com.taki.order.domain.entity.OrderAutoNoDO;
import com.taki.order.enums.OrderAutoTypeEnum;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.manager.OrderNoManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @ClassName OrderNoMangerImpl
 * @Description 订单id 生成 组件
 * @Author Long
 * @Date 2022/1/2 21:23
 * @Version 1.0
 */
@Service
@Slf4j
public class OrderNoMangerImpl implements OrderNoManager {


    @Autowired
    private OrderAutoNoDao orderAutoNoDao;


    @Override
    public String genOrderId(Integer orderNoType, String userId) {

        OrderAutoTypeEnum orderAutoType = OrderAutoTypeEnum.getByCode(orderNoType);

        if (orderAutoType == null){
            throw new ServiceException(OrderErrorCodeEnum.ORDER_TYPE_IS_NULL);
        }
        return orderAutoType + genOrderIdKey(userId);
    }
    
    /*** 
     * @description: 生成订单Id
     * @param userId 用户id
     * @return  订单id
     * @author Long
     * @date: 2022/1/2 22:50
     */ 
    private String genOrderIdKey(String userId) {

        return getDateTimeKey() + getAutoNoKey() +getUserIdKey(userId);
    }

    /***
     * @description: 获取用户Id 后三位
     * @param userId 用户id
     * @return  用户IdKey
     * @author Long
     * @date: 2022/1/2 22:58
     */
    private String getUserIdKey(String userId) {
        // 如果userId的长度大于等于3，则返回
        if (userId.length() >= 3){
            return userId.substring(userId.length() - 3);
        }

        // 如果userId的长度大于等于3，则直接前面补零

        String userIdKey = userId;

        while (userIdKey.length() != 3){
            userIdKey = "0" + userIdKey;
        }

        return userIdKey;
    }

    private String getDateTimeKey() {

        return DateFormatUtils.format(new Date(),"yyMMdd");
    }

    /**
     * @description: 获取自增id
     * @param
     * @return  自增id
     * @author Long
     * @date: 2022/1/2 22:55
     */
    private String getAutoNoKey() {

        OrderAutoNoDO orderAutoNoDO = new OrderAutoNoDO();
        orderAutoNoDao.save(orderAutoNoDO);
        Long orderOn = orderAutoNoDO.getId();;
        return String.valueOf(NumberUtils.genNo(orderOn,8));
    }
}
