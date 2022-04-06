package com.taki.order.wms;

import com.taki.common.enums.OrderStatusChangEnum;
import com.taki.common.enums.OrderStatusEnum;
import com.taki.order.dao.OrderInfoDao;
import com.taki.order.dao.OrderOperateLogDao;
import com.taki.order.domain.dto.OrderInfoDTO;
import com.taki.order.domain.dto.WmsShipDTO;
import com.taki.order.domain.entity.OrderInfoDO;
import com.taki.order.service.impl.OrderOperateLogFactory;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName AbstractWmsShipResultProcessor
 * @Description wms订单物流配送结果处理器
 * @Author Long
 * @Date 2022/4/6 15:08
 * @Version 1.0
 */

public  abstract class AbstractWmsShipResultProcessor implements OrderWmsShipResultProcessor{


    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private OrderOperateLogDao orderOperateLogDao;

    @Autowired
    protected OrderOperateLogFactory orderOperateLogFactory;

    @Override
    public void execute(WmsShipDTO wmsShipDTO) {

        // 查询订单
        OrderInfoDO order = orderInfoDao.getByOrderId(wmsShipDTO.getOrderId());

        if (ObjectUtils.isEmpty(order)){

            return;
        }

        //2，效验 订单状态
        if (!checkOrderStatus(order)){
            return;
        }
        // 执行具体业务逻辑
        doExecute(wmsShipDTO,order);

        //4.更新订单状态
        changeOrderStatus(order,wmsShipDTO);

        // 5.增加操作日志
        saveOrderOperateLog(order,wmsShipDTO);
    }

    /**
     * @description: 保存 订单操作日志
     * @param order 订单 信息
     * @param  wmsShipDTO 物流配送结果请求
     * @return  void
     * @author Long
     * @date: 2022/4/6 15:20
     */
    private   void saveOrderOperateLog(OrderInfoDO order, WmsShipDTO wmsShipDTO){
        orderOperateLogDao.save(orderOperateLogFactory.get(order,wmsShipDTO.getStatusChang()));
    }

    /**
     *更新订单状态
     * @param order 订单 信息
     * @param wmsShipDTO 物流配送结果请求
     */
    private   void changeOrderStatus(OrderInfoDO order, WmsShipDTO wmsShipDTO){

        OrderStatusChangEnum statusChangEnum = wmsShipDTO.getStatusChang();
        orderInfoDao.updateOrderStatus(order.getOrderId(),statusChangEnum.getPreStatus().getCode(),statusChangEnum.getCurrentStatus().getCode());

    }

    /**
     * @description: 执行具体业务逻辑
     * @param wmsShipDTO 物流配送结果请求
     * @param order 订单信息
     * @return  void
     * @author Long
     * @date: 2022/4/6 15:14
     */
    protected abstract void doExecute(WmsShipDTO wmsShipDTO, OrderInfoDO order);

    /**
     * @description: 检查订单状态
     * @param order 订单
     * @return  boolean
     * @author Long
     * @date: 2022/4/6 15:12
     */
    protected abstract boolean checkOrderStatus(OrderInfoDO order);



}
