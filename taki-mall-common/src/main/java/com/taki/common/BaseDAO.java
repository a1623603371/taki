package com.taki.common;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.mapper.Mapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @ClassName BaseDAO
 * @Description DAO 基础类
 * @Author Long
 * @Date 2021/12/14 16:24
 * @Version 1.0
 */
public class BaseDAO <M extends BaseMapper<T>, T> extends ServiceImpl<M,T> {

}
