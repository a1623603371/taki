package com.taki.cookbook.dao;

import com.taki.cookbook.domain.entity.SkuInfoDO;
import com.taki.cookbook.mapper.SkuInfoMapper;
import com.taki.cookbook.service.SkuInfoService;
import com.taki.common.dao.BaseDAO;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 商品表 服务实现类
 * </p>
 *
 * @author long
 * @since 2023-02-18
 */
@Service
public class SkuInfoDAO extends BaseDAO<SkuInfoMapper, SkuInfoDO> implements SkuInfoService {

}
