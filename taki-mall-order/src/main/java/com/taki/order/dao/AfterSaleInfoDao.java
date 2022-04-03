package com.taki.order.dao;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taki.common.BaseDAO;
import com.taki.order.domain.dto.AfterSaleListQueryDTO;
import com.taki.order.domain.dto.AfterSaleOrderListDTO;
import com.taki.order.domain.entity.AfterSaleInfoDO;
import com.taki.order.mapper.AfterSaleInfoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
}
