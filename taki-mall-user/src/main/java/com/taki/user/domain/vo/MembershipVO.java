package com.taki.user.domain.vo;

import com.taki.common.core.AbstractObject;
import io.swagger.annotations.ApiModelProperty;
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

public class MembershipVO extends AbstractObject {


    /**
     * ("主键")
     */

    private Long uid;

    /**
     *   "用户名称")
     */
    private String username;

    /**
     *   ("用户账号")
     */

    private String account;

    /**
     *   ("密码")
     */

    private String password;

    /**
     * ("手机号")
     */
    private String phone;

    /**
     *  ("状态")
     */
    private Integer status;

    /**
     *    ("邮箱")
     */
    private String email;

    /**
     *
     * *("QQ")*/

    private String qq;

    /**
     *  ("微信")
     */
    private String wx;

    /**
     *  ("创建时间")
     */

    private LocalDateTime createTime;

    /**
     *    ("修改时间")
     */
    private LocalDateTime updateTime;

}
