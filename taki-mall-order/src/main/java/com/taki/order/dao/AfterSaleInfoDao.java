package com.taki.order.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taki.common.BaseDAO;
import com.taki.order.domain.dto.AfterSaleListQueryDTO;
import com.taki.order.domain.dto.AfterSaleOrderListDTO;
import com.taki.order.domain.entity.AfterSaleInfoDO;
import com.taki.order.domain.entity.AfterSaleRefundDO;
import com.taki.order.domain.request.CustomerAuditAssembleRequest;
import com.taki.order.mapper.AfterSaleInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @ClassName AfterSaleInfoDao
 * @Description 售後 DAO 組件
 * @Author Long
 * @Date 2022/3/9 15:32
 * @Version 1.0
 */
@Repository
@Slf4j
public class AfterSaleInfoDao extends BaseDAO<AfterSaleInfoMapper, AfterSaleInfoDO> {


    @Autowired
    private AfterSaleInfoMapper afterSaleInfoMapper;

    /**
     * @description: 分页查询售后订单
     * @param query 查询条件
     * @return  售后订单集合
     * @author Long
     * @date: 2022/4/3 20:07
     */
    public Page<AfterSaleOrderListDTO> listByPage(AfterSaleListQueryDTO query){

        log.info("query = {}", JSONObject.toJSONString(query));

        Page<AfterSaleOrderListDTO> page = new Page<>(query.getPageNo(),query.getPageSize());

        return  afterSaleInfoMapper.listByPage(page,query);

    }

    /**
     * @description: 根据售后单Id 查询 售后信息
     * @param afterSaleId 售后单Id
     * @return  售后信息
     * @author Long
     * @date: 2022/4/3 20:08
     */
    public AfterSaleInfoDO getByAfterSaleId(Long afterSaleId) {

        return this.getOne(new QueryWrapper<AfterSaleInfoDO>().eq(AfterSaleInfoDO.AFTER_SALE_ID,afterSaleId));
    }


    public List<AfterSaleInfoDO> listBy(String orderId, ArrayList<Integer> newArrayList) {

        return this.list(new QueryWrapper<AfterSaleInfoDO>().eq(AfterSaleInfoDO.ORDER_ID,orderId).in(AfterSaleInfoDO.AFTER_SALE_TYPE_DETAIL
        ,newArrayList));
    }

    /**
     * @description:  更新 售后单 状态
     * @param afterSaleId 售后单Id
     * @param  fromStatus 变更之前的状态
     * @param toStatus 变更之后的状态
     * @return  void
     * @author Long
     * @date: 2022/4/5 18:06
     */
    public Boolean updateStatus(Long afterSaleId, Integer fromStatus, Integer toStatus) {

        return this.update().set(AfterSaleInfoDO.AFTER_SALE_STATUS,toStatus).eq(AfterSaleInfoDO.AFTER_SALE_ID,afterSaleId)
                .eq(AfterSaleInfoDO.AFTER_SALE_STATUS,toStatus).update();
    }

    /**
     * @description: 根据售后id 查询售后单
     * @param afterSaleId  售后单Id
     * @return
     * @author Long
     * @date: 2022/5/18 15:34
     */
    public AfterSaleInfoDO getOneByAfterSaleId(Long afterSaleId) {
        return this.getOne(new QueryWrapper<AfterSaleInfoDO>().eq(AfterSaleInfoDO.AFTER_SALE_ID,afterSaleId));
    }

    /** 
     * @description:  客服审核售后结果
     * @param afterSaleStatus 售后单状态
     * @param  customerAuditAssembleRequest 客服审核售后单请求
     * @return  void
     * @author Long
     * @date: 2022/5/18 20:53
     */ 
    public Boolean updateCustomerAuditAfterSaleResult(Integer afterSaleStatus, CustomerAuditAssembleRequest customerAuditAssembleRequest) {

        Long afterSaleId = customerAuditAssembleRequest.getAfterSaleId();

        String reviewReason = customerAuditAssembleRequest.getReviewReason();

        Integer reviewReasonCode = customerAuditAssembleRequest.getReviewReasonCode();

        Integer reviewSource = customerAuditAssembleRequest.getReviewSource();

        Date reviewTime = customerAuditAssembleRequest.getReviewTime();

     return    this.update().set(StringUtils.isNotBlank(reviewReason),AfterSaleInfoDO.REVIEW_REASON,reviewReason)
                    .set(ObjectUtils.isNotEmpty(reviewReasonCode),AfterSaleInfoDO.REVIEW_REASON_CODE,reviewReasonCode)
                    .set(ObjectUtils.isNotEmpty(reviewSource),AfterSaleInfoDO.REVIEW_SOURCE,reviewSource)
                    .set(ObjectUtils.isNotEmpty(reviewTime),AfterSaleInfoDO.REVIEW_TIME,reviewTime)
                    .set(AfterSaleInfoDO.AFTER_SALE_STATUS,afterSaleStatus)
                    .eq(AfterSaleInfoDO.AFTER_SALE_ID,afterSaleId).update();


    }
}
