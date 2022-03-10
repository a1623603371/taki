package com.taki.order.dao;

import com.alibaba.fastjson.JSONObject;
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


    public Page<AfterSaleOrderListDTO> listByPage(AfterSaleListQueryDTO query){

        log.info("query = {}", JSONObject.toJSONString(query));

        Page<AfterSaleOrderListDTO> page = new Page<>(query.getPageNo(),query.getPageSize());

        return  afterSaleInfoMapper.listByPage(page,query);

    }
}
