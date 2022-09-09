package com.taki.demo.range;

import com.taki.consistency.custom.query.TaskTimeRangeQuery;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @ClassName MyTaskTimeRangeQuery
 * @Description 自定义最终一致性任务时间范围查询定制
 * @Author Long
 * @Date 2022/9/8 21:49
 * @Version 1.0
 */
@Component
public class MyTaskTimeRangeQuery implements TaskTimeRangeQuery {
    @Override
    public LocalDateTime getStartTime() {
        return LocalDateTime.now().minusHours(12);
    }

    @Override
    public LocalDateTime getEndTime() {
        return LocalDateTime.now();
    }

    @Override
    public Long limitTaskCount() {
        return 200L;
    }


    public static void main(String[] args) {
        System.out.println( LocalDateTime.now().minusHours(12));
    }
}
