package com.taki.order.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.dao.BaseDAO;
import com.taki.order.domain.request.AdjustDeliveryAddressRequest;
import com.taki.order.domain.entity.OrderDeliveryDetailDO;
import com.taki.order.mapper.OrderDeliveryDetailMapper;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

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

    /**
     * @description: 更新订单出库时间
     * @param id 订单Id
     * @param outStockTime 出库时间
     * @return  void
     * @author Long
     * @date: 2022/4/6 15:56
     */
    public Boolean updateOutStockTime(Long id, LocalDateTime outStockTime) {

        return this.update().set(OrderDeliveryDetailDO.OUT_STOCK_TIME,outStockTime).eq(OrderDeliveryDetailDO.ID,id).
                update();
    }

    /**
     * @description: 增加 配送人员信息
     * @param id 配送信息Id
     * @param  delivererNo 配送人员编码
     * @param deliverName 配送人员姓名
     * @param delivererPhone 配送人员手机
     * @return  void
     * @author Long
     * @date: 2022/4/6 16:02
     */
    public Boolean updateDelivery(Long id, String delivererNo, String deliverName, String delivererPhone) {

        return this.update().set(OrderDeliveryDetailDO.DELIVERER_NO,delivererNo)
                            .set(OrderDeliveryDetailDO.DELIVERER_NAME,deliverName)
                            .set(OrderDeliveryDetailDO.DELIVERER_PHONE,delivererPhone)
                            .eq(OrderDeliveryDetailDO.ID,id).update();
    }


}
