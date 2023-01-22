package com.taki.user.mq.event;

import com.taki.user.domain.dto.MembershipDTO;
import lombok.Data;

/**
 * @ClassName UserLoginEvent
 * @Description 用户登录事件
 * @Author Long
 * @Date 2022/10/3 13:17
 * @Version 1.0
 */
@Data
public class UserLoginEvent {


    /**
     * 用户会员信息
     */
    private MembershipDTO membershipDTO;
}
