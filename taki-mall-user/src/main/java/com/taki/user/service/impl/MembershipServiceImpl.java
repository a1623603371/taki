package com.taki.user.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taki.user.domain.MembershipDO;
import com.taki.user.mapper.MembershipMapper;
import com.taki.user.service.MembershipService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author long
 * @since 2021-12-02
 */
@Service
public class MembershipServiceImpl extends ServiceImpl<MembershipMapper, MembershipDO> implements MembershipService {

}
