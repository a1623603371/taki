package com.taki.address.api;

import com.taki.address.domian.dto.AddressDTO;
import com.taki.address.domian.request.AddressQuery;
import com.taki.common.utlis.ResponseData;

/**
 * @ClassName AddressApi
 * @Description 地址服务API
 * @Author Long
 * @Date 2022/1/8 14:16
 * @Version 1.0
 */
public interface AddressApi {


    ResponseData<AddressDTO> queryAddress(AddressQuery addressQuery);
}
