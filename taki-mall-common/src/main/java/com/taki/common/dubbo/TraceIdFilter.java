package com.taki.common.dubbo;

import com.taki.common.constants.CoreConstants;
import com.taki.common.utli.MdcUtil;
import com.taki.common.utli.SnowFlake;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * @ClassName TraceIdFilter
 * @Description 自定义 dubbo filter
 * @Author Long
 * @Date 2022/6/9 13:54
 * @Version 1.0
 */
@Activate(group = {"provider","consumer"})
public class TraceIdFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext rpcContext = RpcContext.getContext();

        String traceId ;

        if (rpcContext.isConsumerSide()){
            traceId = MdcUtil.getTraceId();

            if (traceId == null){
                traceId = SnowFlake.generateIdStr();
            }

            rpcContext.setAttachment(CoreConstants.TRACE_ID,traceId);
        }

        if (rpcContext.isProviderSide()){
            traceId = rpcContext.getAttachment(CoreConstants.TRACE_ID);
            MdcUtil.setTraceId(traceId);
        }

        return invoker.invoke(invocation);
    }
}
