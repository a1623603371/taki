package com.taki.address.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taki.address.domain.entity.AreaDO;
import com.taki.address.mapper.AreaMapper;
import com.taki.common.BaseDAO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * @ClassName AreaDAO
 * @Description 地区 DAO 组件
 * @Author Long
 * @Date 2022/7/31 16:35
 * @Version 1.0
 */
@Repository
public class AreaDAO extends BaseDAO<AreaMapper, AreaDO> {


    /**
     * @description: 查询 地区
     * @param areaCodes 地区编码集合
     * @param area 地区名称
     * @return
     * @author Long
     * @date: 2022/7/31 17:17
     */
    public List<AreaDO> listAreas(Set<String> areaCodes, String area) {
        return  this.list(
                new LambdaQueryWrapper<AreaDO>().eq(
                        StringUtils.isNotEmpty(area),AreaDO::getName,area)
                        .in(!CollectionUtils.isEmpty(areaCodes),AreaDO::getCode,areaCodes));

    }
}
