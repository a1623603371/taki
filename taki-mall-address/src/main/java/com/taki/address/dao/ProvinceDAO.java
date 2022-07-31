package com.taki.address.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.taki.address.domain.entity.ProvinceDO;
import com.taki.address.mapper.ProvinceMapper;
import com.taki.common.BaseDAO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

/**
 * @ClassName ProvinceDAO
 * @Description 省 DAO 组件
 * @Author Long
 * @Date 2022/7/31 16:37
 * @Version 1.0
 */
@Repository
public class ProvinceDAO extends BaseDAO<ProvinceMapper, ProvinceDO> {

    /**
     * @description: 查询省
     * @param provinceCodes 省 编码集合
     * @param  province 省
     * @return
     * @author Long
     * @date: 2022/7/31 19:21
     */
    public List<ProvinceDO> listProvinces(Set<String> provinceCodes, String province) {

        return this.list(new LambdaQueryWrapper<ProvinceDO>()
                .eq(StringUtils.isNotBlank(province),ProvinceDO::getName,province)
                .in(CollectionUtils.isEmpty(provinceCodes),ProvinceDO::getCode,provinceCodes));
    }
}
