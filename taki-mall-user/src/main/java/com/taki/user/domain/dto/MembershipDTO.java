package com.taki.user.domain.dto;

import com.taki.common.core.AbstractObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * <p>
 * 
 * </p>
 *
 * @author long
 * @since 2021-12-02
 */
@Getter
@Setter
@Accessors(chain = true)
@Builder
public class MembershipDTO extends AbstractObject {



    private Long uid;


    private String username;


    private String account;


    private String password;


    private String phone;

    private Integer status;


    private String email;


    private String qq;


    private String wx;


    private LocalDateTime createTime;


    private LocalDateTime updateTime;



}
