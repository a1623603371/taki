package com.taki.persona.service;

import com.taki.persona.domain.dto.PersonaFilterConditionDTO;
import com.taki.persona.domain.page.PersonaConditionPage;
import com.taki.persona.domain.page.PersonaConditionWithIdRange;
import org.bouncycastle.jcajce.provider.symmetric.AES;

import java.util.List;

/**
 * @ClassName PersonaService
 * @Description  筛选人 service 组件
 * @Author Long
 * @Date 2022/10/13 12:13
 * @Version 1.0
 */
public interface PersonaService {


    /*** 
     * @description:  查询最大用户id
     * @param personaFilterCondition 筛选人数据
     * @return  java.lang.Long
     * @author Long
     * @date: 2022/10/13 12:14
     */ 
    Long queryMaxIdByCondition(PersonaFilterConditionDTO personaFilterCondition);


    /***
     * @description: 查询最小用户id
     * @param personaFilterConditionDTO 筛选人数据
     * @return  java.lang.Long
     * @author Long
     * @date: 2022/10/13 12:15
     */
    Long queryMinIdByCondition(PersonaFilterConditionDTO personaFilterConditionDTO);


    /**
     *  根据id范围查询用户Id'
     * @param personaConditionWithIdRange
     * @return
     */
    List<Long> getAccountIdsByIdRange(PersonaConditionWithIdRange personaConditionWithIdRange);


    /*** 
     * @description:  查询用户数量
     * @param personaFilterCondition
     * @return  java.lang.Integer
     * @author Long
     * @date: 2022/10/13 12:20
     */ 
    Integer countByCondition(PersonaFilterConditionDTO  personaFilterCondition);

    /*** 
     * @description:  分页查询用户id
     * @param page 分页查询参数
     * @return
     * @author Long
     * @date: 2022/10/13 12:21
     */ 
    List<Long> getAccountIdsByIdLimit(PersonaConditionPage page);
}
