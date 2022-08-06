package com.taki.tms.service.impl;

import com.taki.common.utli.RandomUtil;
import com.taki.tms.dao.LogisticOrderDao;
import com.taki.tms.domain.dto.PlaceLogisticOrderDTO;
import com.taki.tms.domain.dto.SendOutDTO;
import com.taki.tms.domain.entity.LogisticOrderDO;
import com.taki.tms.domain.request.SendOutRequest;
import com.taki.tms.exception.TmsBizException;
import com.taki.tms.service.LogisticOrderService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName LogisticOrderServiceImpl
 * @Description 发货订单 service 组件
 * @Author Long
 * @Date 2022/5/17 15:11
 * @Version 1.0
 */
@Slf4j
@Service
public class LogisticOrderServiceImpl implements LogisticOrderService {

    @Autowired
    private LogisticOrderDao logisticOrderDao;

    @Override
    public SendOutDTO sendOut(SendOutRequest request) {

        log.info("发货，orderId = {},request={}",request.getOrderId(),request);

        String tmsException = request.getTmsException();

        if (StringUtils.isNotBlank(tmsException) &&  tmsException.equals("true")){
            throw new TmsBizException("发货异常！");
        }

        // 1调用 三方 物流系统 下物流 单子单
        PlaceLogisticOrderDTO result = thirdPartyLogisticApi(request);

        //2 生成物流单
        LogisticOrderDO logisticOrder = request.clone(LogisticOrderDO.class);

        logisticOrder.setLogisticCode(result.getLogisticCode());
        logisticOrder.setContent(result.getContent());

        logisticOrderDao.save(logisticOrder);

        return new SendOutDTO(logisticOrder.getOrderId(),logisticOrder.getLogisticCode()) ;
    }

    @Override
    public Boolean cancelSendOut(String orderId) {
        log.info("取消发货，orderId={}",orderId);
        //查询物流单
        List<LogisticOrderDO> logisticOrders = logisticOrderDao.listByOrderId(orderId);

        logisticOrders.forEach(logisticOrderDO -> {
            logisticOrderDao.removeById(logisticOrderDO);
        });

        return true;
    }

    /** 
     * @description: 调用第三方系统接口 ，下物流单
     * @param request 发货单 请求
     * @return
     * @author Long
     * @date: 2022/5/17 15:29
     */ 
    private PlaceLogisticOrderDTO thirdPartyLogisticApi(SendOutRequest request) {
        // 模拟 第三方物流系统
    String logisticCode = RandomUtil.genRandomNumber(11);
    String content = "测试物流单内容";

    return new PlaceLogisticOrderDTO(logisticCode,content);
    }
}
