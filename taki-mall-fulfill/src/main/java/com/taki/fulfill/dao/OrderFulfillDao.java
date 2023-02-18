package com.taki.fulfill.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.dao.BaseDAO;
import com.taki.fulfill.domain.entity.OrderFulfillDO;
import com.taki.fulfill.mapper.OrderFulfillMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

/**
 * @ClassName OrderFulfillDao
 * @Description 订单 履约表DAO 组件
 * @Author Long
 * @Date 2022/5/15 17:00
 * @Version 1.0
 */

@Repository
public class OrderFulfillDao extends BaseDAO<OrderFulfillMapper, OrderFulfillDO> {


    /** 
     * @description:  保存物流单号
     * @param fulfillId 履约单Id
    @param logisticsCode 物流单号
     * @return  boolean
     * @author Long
     * @date: 2022/5/15 17:03
     */ 
    public boolean saveLogisticsCode(String fulfillId,String  logisticsCode){
    return this.update().set(StringUtils.isNotEmpty(logisticsCode),OrderFulfillDO.LOGISTICS_CODE,logisticsCode)
            .eq(OrderFulfillDO.FULFILL_ID,fulfillId).update();
        
    }


    /** 
     * @description: 根据订单Id 查询 履约表
     * @param orderId
     * @return  com.taki.fulfill.domain.entity.OrderFulfillDO
     * @author Long
     * @date: 2022/5/15 17:10
     */ 
    public OrderFulfillDO getOne(String orderId){
        return this.getOne(new QueryWrapper<OrderFulfillDO>().eq(OrderFulfillDO.ORDER_ID,orderId));
    }

    /**
     * @description: 更新配送员信息
     * @param fulfillId 履约Id
     * @param delivererNo   配送员编码
     * @param  delivererName 配送员 名称
     * @param delivererPhone 配送人员 手机
     * @return  boolean
     * @author Long
     * @date: 2022/5/15 17:11
     */

    public  boolean updateDelivery(String fulfillId,String delivererNo,String delivererName,String delivererPhone){
        return this.update()
                .set(OrderFulfillDO.DELIVERER_NO,delivererNo)
                .set(OrderFulfillDO.DELIVERER_NAME,delivererName)
                .set(OrderFulfillDO.DELIVERER_PHONE,delivererPhone)
                .eq(OrderFulfillDO.FULFILL_ID, fulfillId).update();
    }

}
