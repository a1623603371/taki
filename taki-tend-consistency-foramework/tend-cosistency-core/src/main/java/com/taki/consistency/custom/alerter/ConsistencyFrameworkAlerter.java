package com.taki.consistency.custom.alerter;

import com.taki.consistency.model.ConsistencyTaskInstance;

/**
 * @ClassName ConsistencyFrameworkAlerter
 * @Description 一致性框架告警接口，具体告警通知动作业务由业务服务实现
 *
 * @Author Long
 * @Date 2022/9/3 15:30
 * @Version 1.0
 */
public interface ConsistencyFrameworkAlerter {

    /***
     * @description: 发送告警通知
     * @param consistencyTaskInstance 一致性任务信息实例
     * @return  void
     * @author Long
     * @date: 2022/9/3 15:32
     */
    void sendAlertNotice(ConsistencyTaskInstance consistencyTaskInstance);

}
