package com.taki.user.domain.request;

import lombok.Builder;
import lombok.Data;


import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @ClassName SaveOrUpdateMembershipRequest
 * @Description 保存 或 修改 用户请求参数
 * @Author Long
 * @Date 2022/10/3 20:19
 * @Version 1.0
 */
@Data
@Builder
public class SaveOrUpdateMembershipRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    @NotNull(message = "账号名称【username】不能为空")
    private String userName;


    @NotNull(message = "账号密码【password】不能为空")
    private String password;


    /**
     * 电子邮件
     */
    private String email;

    /**
     * 手机号
     */
    private String phoneNum;

    /**
     * 操作人
     */
    @NotNull(message = "操作人【operateUser】不能为空")
    private Integer operateUser;
}
