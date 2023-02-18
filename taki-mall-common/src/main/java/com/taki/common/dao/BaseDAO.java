package com.taki.common.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.*;

/**
 * @ClassName BaseDAO
 * @Description DAO 基础类
 * @Author Long
 * @Date 2021/12/14 16:24
 * @Version 1.0
 */
public class BaseDAO <M extends BaseMapper<T>, T> extends ServiceImpl<M,T> {

    public List<T> queryByConditions(Map<String,Object> conditions){
        List<T> list = new ArrayList<>();

        if (null ==  conditions) {
            return  list;
        }
        Map<String,Object> where = new HashMap<>();
        Set<String> keys = conditions.keySet();

        keys.forEach(key->{
            if (null ==  conditions.get(key)){
                return;
            }

            where.put(key,conditions.get(key));
        });

        return listByMap(where);
    }

}
