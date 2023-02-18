package com.taki.careerplan.cookbook.converter;

import com.taki.careerplan.cookbook.domain.entity.CookbookDO;
import com.taki.careerplan.domain.dto.CookbookDTO;
import com.taki.careerplan.domain.request.SaveOrUpdateUserRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @ClassName CookBookConverter
 * @Description TODO
 * @Author Long
 * @Date 2023/2/18 17:02
 * @Version 1.0
 */
@Mapper(componentModel = "spring")
public interface CookBookConverter {


    @Mappings({
        @Mapping(target = "cookbookDetail",ignore = true),
         @Mapping(target = "foods",ignore = true)
    })
    CookbookDO convertCookBookDO(SaveOrUpdateUserRequest request);

    @Mappings({
            @Mapping(target = "userName",ignore = true),
            @Mapping(target = "cookbookDetail",ignore = true),
            @Mapping(target = "foods",ignore = true)
    })
    CookbookDTO convertCookBookDTO(CookbookDO cookbook);



    List<CookbookDTO> listConvertCookBookDTO(List<CookbookDO> cookbookDOS);
}
