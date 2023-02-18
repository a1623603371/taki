package com.taki.careerplan.cookbook.converter;

import com.taki.careerplan.cookbook.domain.entity.CookbookUserDO;
import com.taki.careerplan.domain.dto.CookbookUserDTO;
import com.taki.careerplan.domain.request.SaveOrUpdateUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * @ClassName CookBookUserConverter
 * @Description TODO
 * @Author Long
 * @Date 2023/2/18 18:29
 * @Version 1.0
 */
@Mapper(componentModel = "spring")
public interface CookbookUserConverter {


    @Mappings({
            @Mapping(target = "createTime",ignore = true),
            @Mapping(target = "updateTime",ignore = true),
            @Mapping(target = "createUser",ignore = true),
            @Mapping(target = "updateUser",ignore = true)
    })
    CookbookUserDO converterCookbookUserDO(SaveOrUpdateUserRequest request);


    /*** 
     * @description:  对象转换
     * @param cookbookUserDO
     * @return  CookbookUserDTO
     * @author Long
     * @date: 2023/2/18 18:32
     */ 
    CookbookUserDTO converterCookbookUserDTO(CookbookUserDO cookbookUserDO);

}
