package com.taki.market.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.market.domain.entity.FreightTemplateDO;
import com.taki.market.mapper.FreightTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @ClassName FreightTemplateDao
 * @Description 运费模板DAO 组件
 * @Author Long
 * @Date 2022/2/18 11:39
 * @Version 1.0
 */
@Repository
public class FreightTemplateDao extends BaseDAO<FreightTemplateMapper, FreightTemplateDO> {

    @Autowired
    private FreightTemplateMapper freightTemplateMapper;



    public FreightTemplateDO getByRegionId(String regionId) {

        return freightTemplateMapper.selectOne(new QueryWrapper<FreightTemplateDO>().eq(FreightTemplateDO.REGION_ID,regionId));
    }
}
