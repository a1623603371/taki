package com.taki.order.service.impl;

import com.taki.order.dao.AfterSaleLogDAO;
import com.taki.order.domain.entity.AfterSaleInfoDO;
import com.taki.order.domain.entity.AfterSaleLogDO;
import com.taki.order.enums.AfterSaleStatusChannelEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @ClassName AfterSaleOperateLogFactory
 * @Description 售后单操作日志工厂
 * @Author Long
 * @Date 2022/4/5 18:13
 * @Version 1.0
 */
@Component
public class AfterSaleOperateLogFactory {


    @Autowired
    private AfterSaleLogDAO afterSaleLogDAO;


    /** 
     * @description: 获取售后操作日志
     * @param afterSaleInfoDO 售后单信息
     * @param statusChange 状态枚举
     * @return
     * @author Long
     * @date: 2022/4/5 18:28
     */ 
    public AfterSaleLogDO get(AfterSaleInfoDO afterSaleInfoDO, AfterSaleStatusChannelEnum statusChange){

        String operateRemark = statusChange.getOperateRemark();
        Integer preStatus = statusChange.getPreStatus().getCode();
        Integer currentStatus = statusChange.getCurrentStatus().getCode();

        return create(afterSaleInfoDO,preStatus,currentStatus,operateRemark);
    }

    /**
     * @description: 创建售后单 日志
     * @param afterSaleInfoDO 售后单信息
     * @param  preStatus  变更前的状态
     * @param  currentStatus 变跟后的状态
     * @param  operateRemark 操作说明
     * @return  com.taki.order.domain.entity.AfterSaleLogDO
     * @author Long
     * @date: 2022/4/5 18:30
     */
    private AfterSaleLogDO create(AfterSaleInfoDO afterSaleInfoDO, Integer preStatus, Integer currentStatus, String operateRemark) {
        AfterSaleLogDO afterSaleLogDO = new AfterSaleLogDO();

        afterSaleLogDO.setAfterSaleId(afterSaleInfoDO.getAfterSaleId());
        afterSaleLogDO.setPreStatus(preStatus);
        afterSaleLogDO.setCurrentStatus(currentStatus);
        afterSaleLogDO.setRemark(operateRemark);

        return afterSaleLogDO;
    }
}
