package com.taki.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.order.domain.request.AdjustDeliveryAddressRequest;
import com.taki.order.domain.entity.OrderDeliveryDetailDO;
import com.taki.order.mapper.OrderDeliveryDetailMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * @ClassName OrderDeliveryDetailDao
 * @Description 订单地址信息 DAO 组件
 * @Author Long
 * @Date 2022/1/11 21:51
 * @Version 1.0
 */
@Repository
public class OrderDeliveryDetailDao extends BaseDAO<OrderDeliveryDetailMapper, OrderDeliveryDetailDO> {



    /**
     * @description: 根据订单Id查询 订单地址信息
     * @param orderId 订单Id
     * @return  订单地址详细信息
     * @author Long
     * @date: 2022/2/26 20:09
     */
    public OrderDeliveryDetailDO getByOrderId(String orderId) {

        return this.getOne(new QueryWrapper<OrderDeliveryDetailDO>().eq(OrderDeliveryDetailDO.ORDER_ID,orderId));
    }

    /**
     * @description: 修改订单配送地址
     * @param id 订单配送地址详情Id
     * @param  modifyAddressCount 修改次数
     * @param  request 修改订单配送地址请求参数
     * @return  void
     * @author Long
     * @date: 2022/2/26 20:15
     */
    public Boolean updateDeliveryAddress(Long id, Integer modifyAddressCount, AdjustDeliveryAddressRequest request) {

        return   this.update()
                .set(StringUtils.isNotEmpty(request.getProvince()),OrderDeliveryDetailDO.PROVINCE,request.getProvince())
                .set(StringUtils.isNotEmpty(request.getCity()),OrderDeliveryDetailDO.CITY,request.getCity())
                .set(StringUtils.isNotEmpty(request.getArea()),OrderDeliveryDetailDO.AREA,request.getArea())
                .set(StringUtils.isNotEmpty(request.getStreet()),OrderDeliveryDetailDO.STREET,request.getStreet())
                .set(StringUtils.isNotEmpty(request.getDetailAddress()),OrderDeliveryDetailDO.DETAIL_ADDRESS,request.getDetailAddress())
                .set(ObjectUtils.isNotEmpty(request.getLat()),OrderDeliveryDetailDO.LAT,request.getLat())
                .set(ObjectUtils.isNotEmpty(request.getLon()),OrderDeliveryDetailDO.LON,request.getLon())
                .set(OrderDeliveryDetailDO.MODIFY_ADDRESS_COUNT,modifyAddressCount+1)
                .eq(OrderDeliveryDetailDO.ID,id).update();


    }
}
