package com.taki.demo.alertm;

import com.taki.consistency.custom.alerter.ConsistencyFrameworkAlerter;
import com.taki.consistency.model.ConsistencyTaskInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @ClassName NormalAlerter
 * @Description TODO
 * @Author Long
 * @Date 2022/9/5 20:27
 * @Version 1.0
 */
@Component
public class NormalAlerter implements ConsistencyFrameworkAlerter {

    private Logger log = LoggerFactory.getLogger(NormalAlerter.class);

    @Override
    public void sendAlertNotice(ConsistencyTaskInstance consistencyTaskInstance) {
        log.info("执行告警通知逻辑。。。。方法签名为：{}",consistencyTaskInstance.getMethodSignName());

    }
}
