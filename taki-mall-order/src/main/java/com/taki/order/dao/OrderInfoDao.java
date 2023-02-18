package com.taki.order.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taki.common.dao.BaseDAO;
import com.taki.common.enums.OrderStatusEnum;
import com.taki.order.domain.dto.OrderExtJsonDTO;
import com.taki.order.domain.dto.OrderListDTO;
import com.taki.order.domain.dto.OrderListQueryDTO;
import com.taki.order.domain.entity.OrderInfoDO;
import com.taki.order.enums.DeleteStatusEnum;
import com.taki.order.mapper.OrderInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @ClassName OrderInfoDao
 * @Description 订单 DAO 组件
 * @Author Long
 * @Date 2022/1/2 16:10
 * @Version 1.0
 */
@Repository
@Slf4j
public class OrderInfoDao extends BaseDAO<OrderInfoMapper, OrderInfoDO> {


    @Autowired
    private OrderInfoMapper orderInfoMapper;

    /**
     * @description: 根据订单Id查询订单
     * @param orderId 订单Id
     * @return  订单信息
     * @author Long
     * @date: 2022/1/16 15:37
     */
    public OrderInfoDO getByOrderId(String orderId) {

      return  this.getOne(new QueryWrapper<OrderInfoDO>().eq(OrderInfoDO.ORDER_ID,orderId));
    }

    /**
     * @description: 根据订单Id查询子订单
     * @param orderId 订单Id
     * @return  子订单集合
     * @author Long
     * @date: 2022/1/16 15:38
     */
    public List<OrderInfoDO> listByParentOrderId(String orderId) {

        return this.list(new QueryWrapper<OrderInfoDO>().eq(OrderInfoDO.PARENT_ORDER_ID,orderId));

    }

    /**
     * @description: 根据订单id 集合查询 订单
     * @param orderIds 订单id 集合
     * @return  订单集合
     * @author Long
     * @date: 2022/2/26 19:28
     */
    public List<OrderInfoDO> listByOrderIds(List<String> orderIds) {

        return this.listByIds(orderIds);
    }

    /**
     * @description: 根据条件查询订单
     * @param query 订单查询条件
     * @return
     * @author Long
     * @date: 2022/3/3 17:26
     */
    public Page<OrderListDTO> listByPage(OrderListQueryDTO query) {
        log.info("query = {}", JSONObject.toJSONString(query));
        Page<OrderListDTO> page = new Page<>(query.getPageNo(),query.getPageSize());
        return  orderInfoMapper.listByPage(page,query);
    }

    /** 
     * @description: 更新订单扩展信息
     * @param orderId 订单Id
     * @param lackExJson 订单缺品扩展信息
     * @return  void
     * @author Long
     * @date: 2022/3/11 17:42
     */ 
    public Boolean updateOrderExJson(String orderId, OrderExtJsonDTO lackExJson) {
        String exJsonStr = JSONObject.toJSONString(lackExJson);

       return this.update().set(OrderInfoDO.EXT_JSON,exJsonStr).eq(OrderInfoDO.ORDER_ID,orderId).update();
    }

    /**
     * @description: 更新订单状态
     * @param orderId 订单Id
     * @param preStatus 变更前的状态
     * @param currentStatus 当改变的状态
     * @return  void
     * @author Long
     * @date: 2022/4/6 15:18
     */
    public Boolean updateOrderStatus(String orderId, Integer preStatus, Integer currentStatus) {

      return   this.update().set(OrderInfoDO.ORDER_STATUS,currentStatus)
                .eq(OrderInfoDO.ORDER_ID,orderId)
                .eq(OrderInfoDO.ORDER_STATUS,preStatus).update();
    }
    
    /** 
     * @description: 更新 订单
     * @param orderInfoDO
     * @return  void
     * @author Long
     * @date: 2022/5/18 13:35
     */ 
    public Boolean updateOrderInfo(OrderInfoDO orderInfoDO) {

        return this.update(orderInfoDO,new QueryWrapper<OrderInfoDO>().eq(OrderInfoDO.ORDER_ID,orderInfoDO.getOrderId()));
    }
    
    /** 
     * @description: 查询 所有 未支付订单
     * @param 
     * @return  void
     * @author Long
     * @date: 2022/5/20 16:50
     */ 
    public List<OrderInfoDO> listAllUnPaid() {

        return this.list(new QueryWrapper<OrderInfoDO>().eq(OrderInfoDO.ORDER_STATUS, OrderStatusEnum.CREATED));
    }

    /** 
     * @description: 根据订单Id 修改 订单支付信息
     * @param orderInfoDO 订单信息
     * @params orderId 订单Id
     * @return  void
     * @author Long
     * @date: 2022/6/8 14:25
     */ 
    public void updateByOrderId(OrderInfoDO orderInfoDO, String orderId) {
        this.update(orderInfoDO,new QueryWrapper<OrderInfoDO>().eq(OrderInfoDO.ORDER_ID,orderId));

    }

    /**
     * @description:  批量修改 订单 支付信息
     * @param orderInfoDO 订单信息
     * @param   orderIds 订单id 集合
     * @return  void
     * @author Long
     * @date: 2022/6/8 14:39
     */
    public void updateBatchByOrderId(OrderInfoDO orderInfoDO, List<String> orderIds) {
        this.update(orderInfoDO,new QueryWrapper<OrderInfoDO>().in(OrderInfoDO.ORDER_ID,orderIds));
    }

    /**
     * @description: 根据 主订单id 查询 子订单
     * @param orderId 主订单Id
     * @return
     * @author Long
     * @date: 2022/6/8 14:53
     */
    public List<String> listSubOrderIds(String orderId) {
        return this.list(new QueryWrapper<OrderInfoDO>()
                .eq(OrderInfoDO.PARENT_ORDER_ID,orderId))
                .stream().map(OrderInfoDO::getOrderId)
                .collect(Collectors.toList());
    }

    /**
     * @description: 软删除订单
     * @param orderIds 订单id 集合
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/6/8 22:41
     */
    public Boolean softRemoveOrders(List<String> orderIds) {

       return this.update().set(OrderInfoDO.DELETE_STATUS, DeleteStatusEnum.YES).in(OrderInfoDO.ORDER_ID,orderIds).update();
    }
}
