package com.taki.common.mq;

import com.taki.common.constants.CoreConstants;
import com.taki.common.utli.MdcUtil;
import org.apache.rocketmq.common.message.Message;

/**
 * @ClassName MqMessage
 * @Description 自定义扩展mq消息对象
 * @Author Long
 * @Date 2022/6/9 13:48
 * @Version 1.0
 */

public class MQMessage extends Message {


    private static final long serialVersionUID = 774805052017699072L;

    public MQMessage() {
    }

    public MQMessage(String topic, byte[] body) {
        super(topic, body);
        putTraceId();
    }

    public MQMessage(String topic, String tags, String keys, int flag, byte[] body, boolean waitStoreMsgOK) {
        super(topic, tags, keys, flag, body, waitStoreMsgOK);
        putTraceId();
    }

    public MQMessage(String topic, String tags, byte[] body) {
        super(topic, tags, body);
    }

    public MQMessage(String topic, String tags, String keys, byte[] body) {
        super(topic, tags, keys, body);
        putTraceId();
    }

    private void putTraceId(){
        String traceId = MdcUtil.getTraceId();
        if (traceId != null && !"".equals(traceId)){
            super.putUserProperty(CoreConstants.TRACE_ID,traceId);
        }

    }
}
