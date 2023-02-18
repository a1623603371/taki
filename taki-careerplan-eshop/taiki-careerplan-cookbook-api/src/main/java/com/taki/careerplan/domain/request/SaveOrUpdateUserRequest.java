package com.taki.careerplan.domain.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName SaveOrUpdateUserReuquest
 * @Description TODO
 * @Author Long
 * @Date 2023/2/18 16:56
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SaveOrUpdateUserRequest {
    /**
     * 主键
     */
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
     * 个人签名
     */
    private String personal;

    /**
     * 生日
     */
    private String birthday;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 操作人
     */
    private Integer operator;


}
