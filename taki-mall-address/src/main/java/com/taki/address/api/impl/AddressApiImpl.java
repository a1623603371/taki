package com.taki.address.api.impl;

import com.taki.address.api.AddressApi;
import com.taki.address.domian.dto.AddressDTO;
import com.taki.address.domian.request.AddressQuery;
import com.taki.address.exception.AddressException;
import com.taki.address.service.AddressService;
import com.taki.common.utli.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @ClassName AddressApiImpl
 * @Description 用户 收货地址 运程调用 api 接口
 * @Author Long
 * @Date 2022/7/31 16:21
 * @Version 1.0
 */
@DubboService(version = "1.0.0",interfaceClass =AddressApi.class )
@Slf4j
public class AddressApiImpl implements AddressApi {

    @Autowired
    private AddressService addressService;


    @Override
    public ResponseData<AddressDTO> queryAddress(AddressQuery addressQuery) {

        try {

            AddressDTO addressDTO =  addressService.queryAddress(addressQuery);

            return ResponseData.success(addressDTO);

        }catch (AddressException e){
            log.error("biz error",e.getErrorMessage());

            return ResponseData.error(e.getErrorCode(),e.getErrorMessage());
        }
        catch (Exception e) {
            log.error("system error",e.getMessage());
            return ResponseData.error(e.getMessage());
        }

    }
}
