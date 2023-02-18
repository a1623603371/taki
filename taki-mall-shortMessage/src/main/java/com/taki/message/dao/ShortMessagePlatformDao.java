package com.taki.message.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.common.dao.BaseDAO;
import com.taki.message.domian.entity.ShortMessagePlatformDO;
import com.taki.message.mapper.ShortMessagePlatformMapper;;
import org.springframework.stereotype.Repository;

/**
 * @ClassName ShortMessagePlatformDao
 * @Description 短信平台 DAO 组件
 * @Author Long
 * @Date 2021/12/4 16:28
 * @Version 1.0
 */
@Repository
public class ShortMessagePlatformDao extends BaseDAO<ShortMessagePlatformMapper,ShortMessagePlatformDO> {







    public ShortMessagePlatformDO findTypeByOpen(String type) {

        return this.getOne(
                new QueryWrapper<ShortMessagePlatformDO>()
                        .eq(ShortMessagePlatformDO.TYPE,type)
                        .eq(ShortMessagePlatformDO.IS_OPEN,true)
                        .last("limit 1"));
    }

   }
