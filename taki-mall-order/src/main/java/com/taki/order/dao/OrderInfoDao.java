package com.taki.order.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taki.common.BaseDAO;
import com.taki.order.domain.dto.OrderExtJsonDTO;
import com.taki.order.domain.dto.OrderListDTO;
import com.taki.order.domain.dto.OrderListQueryDTO;
import com.taki.order.domain.entity.OrderInfoDO;
import com.taki.order.mapper.OrderInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    public void updateOrderExJson(String orderId, OrderExtJsonDTO lackExJson) {
        String exJsonStr = JSONObject.toJSONString(lackExJson);

        this.update().set(OrderInfoDO.EXT_JSON,exJsonStr).eq(OrderInfoDO.ORDER_ID,orderId).update();
    }
}
