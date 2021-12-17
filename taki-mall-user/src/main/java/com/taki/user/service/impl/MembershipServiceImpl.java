package com.taki.user.service.impl;


import cn.hutool.core.annotation.AnnotationUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.taki.user.dao.MembershipDAO;
import com.taki.user.domain.dto.MembershipDTO;
import com.taki.user.domain.entity.MembershipDO;
import com.taki.user.domain.request.RegisterRequest;
import com.taki.user.mapper.MembershipMapper;
import com.taki.user.service.MembershipService;
import org.springframework.beans.factory.annotation.Autowired;
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
public class MembershipServiceImpl  implements MembershipService {


    @Autowired
    private MembershipDAO membershipDAO;



    @Override
    public MembershipDTO register(RegisterRequest request) {

       return null;
    }
}
