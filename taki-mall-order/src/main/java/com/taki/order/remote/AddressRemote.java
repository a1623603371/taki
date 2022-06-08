package com.taki.order.remote;

import com.taki.address.api.AddressApi;
import com.taki.address.domian.dto.AddressDTO;
import com.taki.address.domian.request.AddressQuery;
import com.taki.common.utlis.ResponseData;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @ClassName AddressRemote
 * @Description 地址服务远程接口
 * @Author Long
 * @Date 2022/6/8 15:52
 * @Version 1.0
 */
@Component
public class AddressRemote {

    @DubboReference(version = "1.0.0")
    private AddressApi addressApi;


    /** 
     * @description:  查询收货地址
     * @param addressQuery 查询收货地址条件  参数
     * @return
     * @author Long
     * @date: 2022/6/8 15:54
     */ 
    public AddressDTO queryAddress(AddressQuery addressQuery){

        ResponseData<AddressDTO> result = addressApi.queryAddress(addressQuery);

        if (result.getSuccess() && result.getData() != null){
            return result.getData();
        }

        return null;


        
    }
}
