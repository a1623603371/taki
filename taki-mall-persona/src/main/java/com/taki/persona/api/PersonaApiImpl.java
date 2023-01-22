package com.taki.persona.api;

import com.taki.common.utli.ResponseData;
import com.taki.persona.domain.dto.PersonaFilterConditionDTO;
import com.taki.persona.domain.page.PersonaConditionPage;
import com.taki.persona.service.PersonaService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @ClassName PersonaApiImpl
 * @Description TODO
 * @Author Long
 * @Date 2022/10/12 22:22
 * @Version 1.0
 */
@DubboService(version = "1.0.0",interfaceClass =PersonaApi.class,retries = 0)
public class PersonaApiImpl implements PersonaApi{


    @Autowired
    private PersonaService personaService;

    @Override
    public ResponseData<Long> queryMaxByCondition(PersonaFilterConditionDTO personaFilterConditionDTO) {
        return null;
    }

    @Override
    public ResponseData<Long> queryMinIdByCondition(PersonaFilterConditionDTO personaFilterConditionDTO) {
        return null;
    }

    @Override
    public ResponseData<List<Long>> getAccountIdsByIdRange(PersonaFilterConditionDTO personaFilterConditionDTO) {
        return null;
    }

    @Override
    public ResponseData<Integer> countByCondition(PersonaFilterConditionDTO personaFilterConditionDTO) {
        return null;
    }

    @Override
    public ResponseData<List<Long>> getAccountIdsByIdLimit(PersonaConditionPage page) {
        return null;
    }
}
