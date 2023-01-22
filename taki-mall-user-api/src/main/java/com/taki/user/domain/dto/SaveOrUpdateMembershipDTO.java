package com.taki.user.domain.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SaveOrUpdateAccuntDTO
 * @Description TODO
 * @Author Long
 * @Date 2022/10/3 13:23
 * @Version 1.0
 */
@Data
public class SaveOrUpdateMembershipDTO implements Serializable {


    private static final long serialVersionUID = 4393262320327542984L;


    /**
     *新增/修改是否成功
     */
    private Boolean success;
}
