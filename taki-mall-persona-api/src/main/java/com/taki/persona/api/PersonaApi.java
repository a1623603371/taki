package com.taki.persona.api;

import com.taki.common.utli.ResponseData;
import com.taki.persona.domain.dto.PersonaFilterConditionDTO;
import com.taki.persona.domain.page.PersonaConditionPage;

import java.util.List;

/**
 * @ClassName PersonaApi
 * @Description TODO
 * @Author Long
 * @Date 2022/10/12 22:15
 * @Version 1.0
 */
public interface PersonaApi {


    /*** 
     * @description:  获取最大用户id
     * @param personaFilterConditionDTO 角色过滤条件实体
     * @return
     * @author Long
     * @date: 2022/10/12 22:15
     */ 
    ResponseData<Long> queryMaxByCondition(PersonaFilterConditionDTO personaFilterConditionDTO);


    /*** 
     * @description:  获取最小用户Id
     * @param personaFilterConditionDTO 角色过滤条件实体
     * @return
     * @author Long
     * @date: 2022/10/12 22:16
     */ 
    ResponseData<Long> queryMinIdByCondition(PersonaFilterConditionDTO personaFilterConditionDTO);



    /*** 
     * @description:  分页获取用户数据
     * @param personaFilterConditionDTO 角色过滤条件实体
     * @return
     * @author Long
     * @date: 2022/10/12 22:19
     */ 
    ResponseData<List<Long>> getAccountIdsByIdRange(PersonaFilterConditionDTO personaFilterConditionDTO);

    /*** 
     * @description:  根据新人条件count值
     * @param personaFilterConditionDTO 角色过滤条件实体
     * @return
     * @author Long
     * @date: 2022/10/12 22:18
     */ 
    ResponseData<Integer> countByCondition(PersonaFilterConditionDTO personaFilterConditionDTO);

    /*** 
     * @description:  分页获取用户数据
     * @param page
     * @return
     * @author Long
     * @date: 2022/10/12 22:20
     */ 
    ResponseData<List<Long>> getAccountIdsByIdLimit(PersonaConditionPage  page);
}
