package com.taki.user.dao;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taki.common.BaseDAO;
import com.taki.user.domain.entity.MembershipDO;
import com.taki.user.mapper.MembershipMapper;
import org.springframework.stereotype.Repository;

/**
 * @ClassName MembershipDAO
 * @Description 用户管理模块DAO
 * @Author Long
 * @Date 2021/12/17 0:40
 * @Version 1.0
 */
@Repository
public class MembershipDAO extends BaseDAO<MembershipMapper, MembershipDO> {
}
