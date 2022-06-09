package com.taki.order.service.impl;

import com.taki.order.dao.AfterSaleInfoDao;
import com.taki.order.dao.AfterSaleItemDao;
import com.taki.order.dao.AfterSaleRefundDAO;
import com.taki.order.dao.OrderInfoDao;
import com.taki.order.domain.dto.OrderLackInfo;
import com.taki.order.domain.entity.OrderInfoDO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName OrderLackProcessor
 * @Description 订单缺品处理器
 * @Author Long
 * @Date 2022/6/9 22:58
 * @Version 1.0
 */
@Component
@Slf4j
public class OrderLackProcessor {

    @Autowired
    private AfterSaleInfoDao afterSaleInfoDao;

    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private AfterSaleRefundDAO afterSaleRefundDAO;

    @Autowired
    private AfterSaleItemDao afterSaleItemDao;

    /**
     * @description: 处理缺品数据
     * @param orderLackInfo
     * @return  void
     * @author Long
     * @date: 2022/6/9 23:02
     */
    public void saveLackInfo(OrderLackInfo orderLackInfo){

        //1.存储 售后单 ，item 和 售后单
        afterSaleInfoDao.save(orderLackInfo.getLackAfterSaleOrder());

        afterSaleItemDao.saveBatch(orderLackInfo.getAfterSaleItems());

        afterSaleRefundDAO.save(orderLackInfo.getAfterSaleRefundDO());
        orderInfoDao.updateOrderExJson(orderLackInfo.getOrderId(), orderLackInfo.getLackExJson());



    }
}
