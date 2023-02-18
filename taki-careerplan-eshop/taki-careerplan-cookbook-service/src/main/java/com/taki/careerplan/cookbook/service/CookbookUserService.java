package com.taki.careerplan.cookbook.service;

import com.taki.careerplan.domain.dto.CookbookUserDTO;
import com.taki.careerplan.domain.dto.SaveOrUpdateUserDTO;
import com.taki.careerplan.domain.request.CookbookUserQueryRequest;
import com.taki.careerplan.domain.request.SaveOrUpdateUserRequest;

/**
 * <p>
 * 菜谱作者表 服务类
 * </p>
 *
 * @author long
 * @since 2023-02-18
 */
public interface CookbookUserService  {

    /***
     * @description:  保存 或 修改 菜谱作者
     * @param request 菜谱作者请求数据
     * @return
     * @author Long
     * @date: 2023/2/18 16:59
     */
    SaveOrUpdateUserDTO saveOrUpdateUser(SaveOrUpdateUserRequest request);

    /***
     * @description: 获取菜谱用户信息
     * @param request 获取 菜谱信息请求
     * @return  com.taki.careerplan.domain.dto.CookbookUserDTO
     * @author Long
     * @date: 2023/2/18 21:27
     */
    CookbookUserDTO getUserInfo(CookbookUserQueryRequest request);
}
