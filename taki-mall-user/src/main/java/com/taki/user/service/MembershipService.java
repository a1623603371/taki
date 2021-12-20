package com.taki.user.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.taki.user.domain.dto.MembershipDTO;
import com.taki.user.domain.entity.MembershipDO;
import com.taki.user.domain.request.RegisterRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author long
 * @since 2021-12-02
 */
public interface MembershipService  {

    /**
     * @description: 注册会员
     * @param request 注册请求实体
     * @return  Boolean
     * @author Long
     * @date: 2021/12/17 17:13
     */
    Boolean register(RegisterRequest request);

    MembershipDTO getUserInfo(Long uid);
}
