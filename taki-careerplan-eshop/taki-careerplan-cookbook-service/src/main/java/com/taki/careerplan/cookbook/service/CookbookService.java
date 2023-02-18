package com.taki.careerplan.cookbook.service;

import com.taki.careerplan.domain.dto.SaveOrUpdateCookbookDTO;
import com.taki.careerplan.domain.request.SaveOrUpdateCookbookRequest;

/**
 * <p>
 * 菜谱表 服务类
 * </p>
 *
 * @author long
 * @since 2023-02-18
 */
public interface CookbookService {

    /***
     * @description:  保存  or 修改 菜谱
     * @param request
     * @return  com.taki.common.utli.ResponseData<SaveOrUpdateCookbookDTO>
     * @author Long
     * @date: 2023/2/18 22:50
     */
    SaveOrUpdateCookbookDTO saveOrUpdateCookbook(SaveOrUpdateCookbookRequest request);
}
