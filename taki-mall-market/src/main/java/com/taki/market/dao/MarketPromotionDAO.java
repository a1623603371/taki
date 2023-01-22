package com.taki.market.dao;

import com.taki.common.BaseDAO;
import com.taki.market.domain.entity.MarketFreightTemplateDO;
import com.taki.market.domain.entity.MarketPromotionDO;
import com.taki.market.mapper.MarketFreightTemplateMapper;
import com.taki.market.mapper.MarketPromotionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @ClassName MarketPromotionDAO
 * @Description 营销 促销活动 DAO 组件
 * @Author Long
 * @Date 2022/9/24 22:47
 * @Version 1.0
 */
@Repository
public class MarketPromotionDAO extends BaseDAO<MarketPromotionMapper, MarketPromotionDO> {


    @Autowired
    private MarketPromotionMapper marketPromotionMapper;

    /*** 
     * @description: 发布促销活动
     * @param marketPromotionDO 营销促销实体
     * @return  java.lang.Boolean
     * @author Long
     * @date: 2022/9/29 21:37
     */ 
    public Boolean saveOrUpdatePromotion(MarketPromotionDO marketPromotionDO){
        return  this.save(marketPromotionDO);
    }


}
