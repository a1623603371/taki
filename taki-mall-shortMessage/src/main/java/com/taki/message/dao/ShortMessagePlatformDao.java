package com.taki.message.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.taki.message.domian.ShortMessagePlatformDO;
import com.taki.message.dao.org.service.MyShortMessagePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * @ClassName ShortMessagePlatformDao
 * @Description 短信平台 DAO 组件
 * @Author Long
 * @Date 2021/12/4 16:28
 * @Version 1.0
 */
@Repository
public class ShortMessagePlatformDao {

    @Autowired
    private MyShortMessagePlatformService myShortMessagePlatformService;

    public Boolean save(ShortMessagePlatformDO shortMessagePlatformDO) {
     return    myShortMessagePlatformService.save(shortMessagePlatformDO);
    }

    public Boolean update(ShortMessagePlatformDO shortMessagePlatformDO) {
     return   myShortMessagePlatformService.updateById(shortMessagePlatformDO);
    }

    public ShortMessagePlatformDO findTypeByOpen(String type) {

        return myShortMessagePlatformService.getOne(
                new QueryWrapper<ShortMessagePlatformDO>()
                        .eq(ShortMessagePlatformDO.TYPE,type)
                        .eq(ShortMessagePlatformDO.IS_OPEN,true)
                        .last("limit 1"));
    }

    public ShortMessagePlatformDO findById(Long id) {
        return myShortMessagePlatformService.getById(id);
    }
}
