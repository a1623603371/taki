package com.taki.address.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.address.domain.entity.StreetDO;
import com.taki.address.mapper.StreetMapper;
import com.taki.common.BaseDAO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName StreetDAO
 * @Description 街道 DAO 组件
 * @Author Long
 * @Date 2022/7/31 16:37
 * @Version 1.0
 */
@Repository
public class StreetDAO extends BaseDAO<StreetMapper, StreetDO> {

    /**
     * @description: 查询街道信息
     * @param streetCode 街道
     * @param street 街道信息
     * @return
     * @author Long
     * @date: 2022/7/31 16:59
     */
    public List<StreetDO> listStreets(String streetCode, String street) {

       return this.list(new LambdaQueryWrapper<StreetDO>()
               .eq(StringUtils.isNotEmpty(streetCode),StreetDO::getCode,streetCode)
                .eq(StringUtils.isNotEmpty(street),StreetDO::getName,street));

    }
}
