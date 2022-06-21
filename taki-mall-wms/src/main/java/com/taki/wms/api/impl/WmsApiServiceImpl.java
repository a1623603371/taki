package com.taki.wms.api.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.taki.common.utlis.ObjectUtil;
import com.taki.common.utlis.RandomUtil;
import com.taki.common.utlis.ResponseData;
import com.taki.wms.api.WmsApi;
import com.taki.wms.dao.DeliverOrderDao;
import com.taki.wms.dao.DeliveryOrderItemDao;
import com.taki.wms.domain.dto.PickDTO;
import com.taki.wms.domain.dto.ScheduleDeliveryResult;
import com.taki.wms.domain.entity.DeliverOrderDO;
import com.taki.wms.domain.entity.DeliveryOrderItemDO;
import com.taki.wms.domain.request.PickGoodsRequest;
import com.taki.wms.exception.WmsBizException;
import com.taki.wms.exception.WmsErrorCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.LifecycleState;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

/**
 * @ClassName WmsApiService
 * @Description WMS 服务
 * @Author Long
 * @Date 2022/5/16 18:01
 * @Version 1.0
 */
@DubboService(version = "1.0.0",interfaceClass = WmsApi.class,retries = 0)
@Slf4j
public class WmsApiServiceImpl implements WmsApi {


    @Autowired
    private DeliverOrderDao deliverOrderDao;

    @Autowired
    private DeliveryOrderItemDao deliveryOrderItemDao;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResponseData<PickDTO> pickGoods(PickGoodsRequest request) {
        log.info("拣货 ，orderId={}，request={}",request.getOrderId(),request);
        String wmsException = request.getWmsException();

        if (wmsException.equals("true")){
            throw new WmsBizException(WmsErrorCodeEnum.DELIVERY_ORDER_ID_GEN_ERROR);
        }
        //1.拣货 调度出库
        ScheduleDeliveryResult result = scheduleDelivery(request);

        //2.存储出库单和出库单条目
        deliverOrderDao.save(result.getDeliveryOrder());
        deliveryOrderItemDao.saveBatch(result.getDeliveryOrderItems());

        // 3.构造返回参数
        return ResponseData.success(new PickDTO(request.getOrderId()));
    }
    
    /** 
     * @description: 调度出库
     * @param request 出库单 请求
     * @return
     * @author Long
     * @date: 2022/5/17 14:12
     */ 
    private ScheduleDeliveryResult scheduleDelivery(PickGoodsRequest request) {
        log.info("orderId={}的订单进行调度出库",request.getOrderId());
        // 1.生成出库单Id
        String deliveryOrderId = genDeliveryOrderId();
        //2.生成出库单
        DeliverOrderDO deliverOrder = request.clone(DeliverOrderDO.class);
        deliverOrder.setDeliveryOrderId(deliveryOrderId);

        // 3.生成出库单条目
        List<DeliveryOrderItemDO> deliveryOrderItems = ObjectUtil.convertList(request.getOrderItemRequests(),DeliveryOrderItemDO.class);
        deliveryOrderItems.forEach(deliveryOrderItemDO -> {
            deliveryOrderItemDO.setDeliveryOrderId(deliveryOrderId);
        });

        // 4. sku调度出库
        // 这里仅仅只是模拟，假设有一个无限货物的仓库货柜(id = 1)
        for(DeliveryOrderItemDO item : deliveryOrderItems) {
            item.setPickingCount(item.getSaleQuantity());
            item.setSkuContainerId(1);
        }
        return new ScheduleDeliveryResult(deliverOrder,deliveryOrderItems);
    }
    
    /** 
     * @description:  生成履约单Id
     * @param 
     * @return
     * @author Long
     * @date: 2022/5/17 14:14
     */ 
    private String genDeliveryOrderId() {
        return RandomUtil.genRandomNumber(10);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseData<Boolean> cancelPickGoods(String orderId) {
        log.info("取消拣货，orderId={}",orderId);

        //1.查询出库单
    List<DeliverOrderDO> deliverOrders = deliverOrderDao.listByOrderId(orderId);

        //2.移除出库单和条目
        if (CollectionUtils.isNotEmpty(deliverOrders)){

            deliverOrders.forEach(deliverOrderDO -> {
                List<DeliveryOrderItemDO> deliveryOrderItems = deliveryOrderItemDao.listByDeliveryOrderId(deliverOrderDO.getDeliveryOrderId());

                deliveryOrderItems.forEach(deliveryOrderItemDO -> {
                    deliveryOrderItemDao.removeById(deliveryOrderItemDO);
                });

                deliverOrderDao.removeById(deliverOrderDO);

            });
        }
        return ResponseData.success(true);
    }
}
