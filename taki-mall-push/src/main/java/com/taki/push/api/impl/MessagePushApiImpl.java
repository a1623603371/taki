package com.taki.push.api.impl;

import com.taki.common.utli.ResponseData;
import com.taki.push.api.MessagePushApi;
import com.taki.push.domain.dto.QueryMessageDTO;
import com.taki.push.domain.dto.SaveOrUpdateMessageDTO;
import com.taki.push.domain.dto.SendMessageDTO;
import com.taki.push.domain.request.QueryMessageRequest;
import com.taki.push.domain.request.SaveOrUpdateMessageRequest;
import com.taki.push.domain.request.SendMessageByAccountRequest;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.annotation.DubboService;

import java.util.List;

/**
 * @ClassName MessagePushApiImpl
 * @Description TODO
 * @Author Long
 * @Date 2022/10/5 16:20
 * @Version 1.0
 */
@DubboService(version = "1.0.0",interfaceClass = MessagePushApi.class,retries = 0)
public class MessagePushApiImpl implements MessagePushApi {




    @Override
    public ResponseData<SaveOrUpdateMessageDTO> saveOrUpdateMessage(SaveOrUpdateMessageRequest saveOrUpdateMessageRequest) {
        return null;
    }

    @Override
    public ResponseData<SendMessageDTO> sendMessageByAccount(SendMessageByAccountRequest sendMessageByAccountRequest) {
        return null;
    }

    @Override
    public ResponseData<List<QueryMessageDTO>> queryMessageByCondition(QueryMessageRequest queryMessageRequest) {
        return null;
    }
}
