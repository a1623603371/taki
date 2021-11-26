package com.taki.user.service.impl;

import com.taki.user.domain.UserDO;
import com.taki.user.mapper.UserMapper;
import com.taki.user.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author long
 * @since 2021-11-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserDO> implements UserService {

}
