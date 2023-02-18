package com.taki.address.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taki.address.domain.entity.CityDO;
import com.taki.address.mapper.CityMapper;
import com.taki.common.dao.BaseDAO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * @ClassName CityDAO
 * @Description 城市 DAO 组件
 * @Author Long
 * @Date 2022/7/31 16:36
 * @Version 1.0
 */
@Repository
public class CityDAO extends BaseDAO<CityMapper, CityDO> {

    /**
     * @description: 查询市
     * @param cityCodes 市 编码集合
     * @param city 市
     * @return  =
     * @author Long
     * @date: 2022/7/31 18:28
     */
    public List<CityDO> listCities(Set<String> cityCodes, String city) {
        return this.list(new LambdaQueryWrapper<CityDO>()
                .eq(StringUtils.isNotEmpty(city),CityDO::getName,city)
                .in(!CollectionUtils.isEmpty(cityCodes),CityDO::getCode,cityCodes));
    }
}
