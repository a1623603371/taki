package com.taki.market.remote;

import com.taki.common.utli.ResponseData;
import com.taki.market.exception.MarketBizException;
import com.taki.user.api.MembershipApi;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

/**
 * @ClassName MembershipRemote
 * @Description 用户会员 运程调用
 * @Author Long
 * @Date 2022/10/3 21:41
 * @Version 1.0
 */
@Service
@Slf4j
public class MembershipRemote {


    @DubboReference(version = "1.0.0",retries = 0)
    private MembershipApi membershipApi;



    public Long queryMaxUserId(){

        ResponseData<Long>  responseData = membershipApi.queryMaxUserId();

        if (!responseData.getSuccess()){
            log.error("查询用户会员最大id出错:{}",responseData.getMessage());
            throw new MarketBizException(responseData.getCode(),responseData.getMessage());
        }

        return responseData.getData();


    }
}

