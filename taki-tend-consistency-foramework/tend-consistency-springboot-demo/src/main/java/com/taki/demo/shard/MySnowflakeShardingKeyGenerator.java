package com.taki.demo.shard;

import com.google.common.base.Preconditions;
import com.taki.consistency.custom.shard.ShardingKeyGenerator;
import com.taki.consistency.custom.shard.TimeService;
import com.taki.consistency.util.TimeUtils;
import lombok.Setter;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName MySnowflakeShardingkeyGenerator
 * @Description TODO
 * @Author Long
 * @Date 2022/9/9 15:43
 * @Version 1.0
 */

public class MySnowflakeShardingKeyGenerator implements ShardingKeyGenerator {

    private static  final long EPOCH;


    private static final  long SEQUENCE_BITS = 12L;

    private static final  long WORKER_ID_BITS = 10L;

    private static final  long SEQUENCE_MASK = (1 << SEQUENCE_BITS) - 1;

    private static final long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;

    private static final long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;

    private static final long WORKER_ID_MAX_VALUE = 1L << WORKER_ID_BITS;

    private static final long WORKER_ID = 0;

    private static final long MAX_TOLERATE_TIME_DIFFERENCE_MILLISECONDS = 10;


    @Setter
    private static TimeService timeService = new TimeService();
    @Setter
    private Properties properties = new Properties();

    private byte sequenceOffset;

    private long sequence;

    private long lastMilliseconds;

    static {


        EPOCH = LocalDateTime.of(2016,1,1,0,0,0).atZone(ZoneOffset.systemDefault()).toEpochSecond();
        System.out.println(LocalDateTime.of(2016,1,1,0,0,0).toInstant(ZoneOffset.of("+8")).toEpochMilli());
    }

    /*** 
     * @description:  生产一致性任务分片键
     * @param 
     * @return  一致性任务分片键
     * @author Long
     * @date: 2022/9/9 16:31
     */ 
    @Override
    public Long generateShardKey() {
        long currentMilliseconds = timeService.getCurrentMillis();
        if (waitTolerateTimeDifferenceIfNeed(currentMilliseconds)){
             currentMilliseconds = timeService.getCurrentMillis();
        }
        if (lastMilliseconds == currentMilliseconds){
            if (0L == (sequence = (sequence + 1) & SEQUENCE_MASK)){
                currentMilliseconds = waitUntilNextTime(currentMilliseconds);
            }
        }else {
            vibrateSequenceOffset();
            sequence = sequenceOffset;
        }
        lastMilliseconds = currentMilliseconds;

        return ((currentMilliseconds - EPOCH) << TIMESTAMP_LEFT_SHIFT_BITS) | (getWorkerId()) ;
    }

    private long getWorkerId() {
        long result = Long.valueOf(properties.getProperty("worker.id",String.valueOf(WORKER_ID)));
        Preconditions.checkArgument(result>=0L && result < WORKER_ID_MAX_VALUE);
        return result;
    }

    private void vibrateSequenceOffset() {
        sequenceOffset = (byte) (~sequenceOffset & 1);
    }

    private long waitUntilNextTime(final long lastTime) {
        long result = timeService.getCurrentMillis();

        while (result <= lastTime){
            result = timeService.getCurrentMillis();
        }
        return result;
    }

    @SneakyThrows
    private boolean waitTolerateTimeDifferenceIfNeed(long currentMilliseconds) {
        if (lastMilliseconds <= currentMilliseconds){
            return false;
        }
        long timeDifferenceMilliseconds = lastMilliseconds  - currentMilliseconds;
        Preconditions.checkState(timeDifferenceMilliseconds < getMaxTolerateTimeDifferenceMilliseconds(),
                "Clock is moving backwards,last time is %d  milliseconds,current time %d milliseconds",lastMilliseconds,currentMilliseconds);
        Thread.sleep(timeDifferenceMilliseconds);

        return true;

    }

    private long getMaxTolerateTimeDifferenceMilliseconds() {

        return Integer.valueOf(properties.getProperty("max.tolerate.time.difference.milliseconds",String.valueOf(MAX_TOLERATE_TIME_DIFFERENCE_MILLISECONDS)));
    }


    public static void main(String[] args) {

    }
}
