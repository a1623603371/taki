package com.taki.wms.service.impl;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.taki.common.utli.ObjectUtil;
import com.taki.common.utli.RandomUtil;
import com.taki.wms.dao.DeliverOrderDao;
import com.taki.wms.dao.DeliveryOrderItemDao;
import com.taki.wms.domain.dto.PickDTO;
import com.taki.wms.domain.dto.ScheduleDeliveryResult;
import com.taki.wms.domain.entity.DeliveryOrderDO;
import com.taki.wms.domain.entity.DeliveryOrderItemDO;
import com.taki.wms.domain.request.PickGoodsRequest;
import com.taki.wms.exception.WmsBizException;
import com.taki.wms.exception.WmsErrorCodeEnum;
import com.taki.wms.service.WmsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName WmsServiceImpl
 * @Description 库存 service 组件
 * @Author Long
 * @Date 2022/7/31 21:34
 * @Version 1.0
 */
@Service
@Slf4j
public class WmsServiceImpl implements WmsService {

    @Autowired
    private DeliverOrderDao deliverOrderDao;

    @Autowired
    private DeliveryOrderItemDao deliveryOrderItemDao;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public PickDTO pickGoods(PickGoodsRequest request) {
        log.info("拣货 ，orderId={}，request={}",request.getOrderId(),request);
        String wmsException = request.getWmsException();

        if (StringUtils.isNotBlank(wmsException) &&wmsException.equals("true")){
            throw new WmsBizException(WmsErrorCodeEnum.DELIVERY_ORDER_ID_GEN_ERROR);
        }
        //1.拣货 调度出库
        ScheduleDeliveryResult result = scheduleDelivery(request);

        //2.存储出库单和出库单条目
        deliverOrderDao.save(result.getDeliveryOrder());
        deliveryOrderItemDao.saveBatch(result.getDeliveryOrderItems());

        // 3.构造返回参数
        return new PickDTO(request.getOrderId());
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
        DeliveryOrderDO deliverOrder = request.clone(DeliveryOrderDO.class);
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
    public Boolean cancelPickGoods(String orderId) {
        log.info("取消拣货，orderId={}",orderId);

        //1.查询出库单
        List<DeliveryOrderDO> deliverOrders = deliverOrderDao.listByOrderId(orderId);

        //2.移除出库单和条目
        if (CollectionUtils.isNotEmpty(deliverOrders)){

            List<Long> orderIds = deliverOrders.stream().map(DeliveryOrderDO::getId).collect(Collectors.toList());

            deliverOrderDao.removeByIds(orderIds);

            //删除条目

            List<String> deliveryOrderIds = deliverOrders.stream().map(DeliveryOrderDO::getDeliveryOrderId).collect(Collectors.toList());
            List<DeliveryOrderItemDO> deliveryOrderItems = deliveryOrderItemDao.listByDeliveryOrderIds(deliveryOrderIds);
            if (CollectionUtils.isNotEmpty(deliveryOrderItems)){
                deliveryOrderItemDao.removeByIds(deliveryOrderItems);
            }



        }
        return true;
    }
}
