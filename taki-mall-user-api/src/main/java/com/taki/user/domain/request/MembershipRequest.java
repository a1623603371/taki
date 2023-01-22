package com.taki.user.domain.request;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName MembershipRequest
 * @Description TODO
 * @Author Long
 * @Date 2022/10/3 20:17
 * @Version 1.0
 */
@Data
@Builder
public class MembershipRequest implements Serializable {


    private static final long serialVersionUID = -955083236604531875L;

    /**
     *  账号Id
     */
    private Integer id;

    /**
     * 用户名称
     */
    private String userName;
}
