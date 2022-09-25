package com.taki.market.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.BaseDAO;
import com.taki.market.domain.entity.MarketCouponConfigDO;
import com.taki.market.domain.entity.MarketFreightTemplateDO;
import com.taki.market.mapper.MarketCouponConfigMapper;
import com.taki.market.mapper.MarketFreightTemplateMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @ClassName MarketFreightTemplateDAO
 * @Description 营销 运费模板DAO 组件
 * @Author Long
 * @Date 2022/9/24 22:44
 * @Version 1.0
 */
@Repository
public class MarketFreightTemplateDAO extends BaseDAO<MarketFreightTemplateMapper, MarketFreightTemplateDO> {


    @Autowired
    private MarketFreightTemplateMapper marketFreightTemplateMapper;


    public MarketFreightTemplateDO getByRegionId(String regionId) {

        return marketFreightTemplateMapper.selectOne(new QueryWrapper<MarketFreightTemplateDO>().eq(MarketFreightTemplateDO.REGION_ID,regionId));
    }
}
