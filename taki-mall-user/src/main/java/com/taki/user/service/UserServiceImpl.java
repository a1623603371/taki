package com.taki.user.service;

import com.taki.core.enums.CodeEnum;
import com.taki.core.error.ServiceException;
import org.springframework.stereotype.Service;

/**
 * @ClassName UserServiceImpl
 * @Description TODO
 * @Author Long
 * @Date 2021/11/25 14:05
 * @Version 1.0
 */
@Service
public class UserServiceImpl {



    public  String test() throws ServiceException {

        try {
            int a = 1 / 0 ;
        }catch (Exception e){
            throw new ServiceException(CodeEnum.SYSTEM_ERROR,"11111",null);
        }

        return "test";
    }
}
