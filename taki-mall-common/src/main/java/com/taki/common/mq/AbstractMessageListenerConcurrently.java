package com.taki.common.mq;

import com.taki.common.core.CoreConstants;
import com.taki.common.utlis.MdcUtil;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AbstractMessageListenerConcurrently
 * @Description 抽象消费者M
 * @Author Long
 * @Date 2022/6/9 12:45
 * @Version 1.0
 */
public abstract class AbstractMessageListenerConcurrently implements MessageListenerConcurrently {

    @Override
    public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

        try {
            if (!CollectionUtils.isEmpty(msgs))
            {
                Map<String,String> map = msgs.get(0).getProperties();
                String traceId = "";

                if (map == null){
                    traceId = map.get(CoreConstants.TRACE_ID);
                }

                if (traceId!= null && !"".equals(traceId)){
                    MdcUtil.setTraceId(traceId);
                }

            }
            return  omMessage(msgs,consumeConcurrentlyContext);

        }finally {
            MdcUtil.removeTraceId();
        }



    }

    protected abstract ConsumeConcurrentlyStatus omMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext);
}
