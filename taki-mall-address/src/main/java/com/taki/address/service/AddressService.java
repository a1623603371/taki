package com.taki.address.service;

import com.taki.address.domain.entity.StreetDO;
import com.taki.address.domian.dto.AddressDTO;
import com.taki.address.domian.request.AddressQuery;

import java.util.List;

/**
 * @ClassName AddressService
 * @Description 地址 service 服务
 * @Author Long
 * @Date 2022/7/31 16:47
 * @Version 1.0
 */
public interface AddressService {

    /**
     * @description: 查询用户收货地址
     * @param query 收货地址查询条件
     * @return
     * @author Long
     * @date: 2022/7/31 17:05
     */
    AddressDTO queryAddress(AddressQuery query);

}
