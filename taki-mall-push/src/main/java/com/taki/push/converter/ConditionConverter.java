package com.taki.push.converter;

import com.taki.push.domain.dto.PersonaFilterConditionDTO;
import com.taki.user.domain.dto.MemberFilterDTO;
import com.taki.user.domain.dto.MembershipFilterConditionDTO;
import org.mapstruct.Mapper;

/**
 * @ClassName ConditionConverter
 * @Description TODO
 * @Author Long
 * @Date 2022/10/5 21:26
 * @Version 1.0
 */
@Mapper(componentModel = "spring")
public interface ConditionConverter {

    PersonaFilterConditionDTO converterFilterCondition(MembershipFilterConditionDTO membershipFilterConditionDTO);



    PersonaFilterConditionDTO converterFilterCondition(MemberFilterDTO memberFilterDTO);

}
