package com.taki.careerplan.domain.dto;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName CookbookUserDTO
 * @Description TODO
 * @Author Long
 * @Date 2023/2/18 18:32
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
public class CookbookUserDTO implements Serializable {


    private static final long serialVersionUID = -862169787507828454L;

    private Long id;
    /**
     * 作者名称
     */

    private String userName;

    /**
     * 头像
     */

    private String profile;

    /**
     * 个⼈签名
     */

    private String personal;

    /**
     * ⽣⽇
     */

    private String birthday;

    /**
     * 性别
     */

    private Integer sex;

    /**
     * 创建⼈
     */

    private Integer createUser;

    /**
     * 修改⼈
     */

    private Integer updateUser;
}
