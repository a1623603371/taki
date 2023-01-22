package com.taki.persona.service.impl;

import com.taki.persona.dao.MembershipFilterDAO;
import com.taki.persona.domain.dto.PersonaFilterConditionDTO;
import com.taki.persona.domain.page.PersonaConditionPage;
import com.taki.persona.domain.page.PersonaConditionWithIdRange;
import com.taki.persona.service.PersonaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @ClassName PersonaServiceImpl
 * @Description 筛选人 service 组件
 * @Author Long
 * @Date 2022/10/13 12:21
 * @Version 1.0
 */
@Service
@Slf4j
public class PersonaServiceImpl implements PersonaService {

    @Autowired
     private MembershipFilterDAO membershipFilterDAO;


    @Override
    public Long queryMaxIdByCondition(PersonaFilterConditionDTO personaFilterCondition) {
        return null;
    }

    @Override
    public Long queryMinIdByCondition(PersonaFilterConditionDTO personaFilterConditionDTO) {
        return null;
    }

    @Override
    public List<Long> getAccountIdsByIdRange(PersonaConditionWithIdRange personaConditionWithIdRange) {
        return null;
    }

    @Override
    public Integer countByCondition(PersonaFilterConditionDTO personaFilterCondition) {
        return null;
    }

    @Override
    public List<Long> getAccountIdsByIdLimit(PersonaConditionPage page) {
        return null;
    }
}
