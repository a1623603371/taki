package com.taki.user.converter;

import com.taki.user.domain.dto.MembershipDTO;
import com.taki.user.domain.entity.MembershipDO;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @ClassName MembershipConverter
 * @Description TODO
 * @Author Long
 * @Date 2022/10/3 20:46
 * @Version 1.0
 */
@Mapper(componentModel = "spring")
public interface MembershipConverter {

    /*** 
     * @description: 批量
     * @param membershipDO
     * @return  java.util.List<com.taki.user.domain.dto.MembershipDTO>
     * @author Long
     * @date: 2022/10/3 20:47
     */ 
    List<MembershipDTO> listEntityToDTO(List<MembershipDO> membershipDO);

    MembershipDTO entityToDTO(MembershipDO membershipDO);
}
