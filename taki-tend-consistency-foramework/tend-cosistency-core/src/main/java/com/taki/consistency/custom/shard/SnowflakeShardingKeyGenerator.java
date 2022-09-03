package com.taki.consistency.custom.shard;

import com.google.common.base.Preconditions;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.Calendar;
import java.util.Properties;

/**
 * @ClassName SnowflakeShardingKeyGenerator
 * @Description 任务分片键生成器实现类
 * 如业务服务在配置文件中，没有配置任务分片j键生成器的实现类，则使用该类分片键生成器
 * @Author Long
 * @Date 2022/9/3 20:53
 * @Version 1.0
 */
public class SnowflakeShardingKeyGenerator implements ShardingKeyGenerator{

    private static volatile SnowflakeShardingKeyGenerator instance;

    public static final long EPOCH;

    private static final long SEQUENCE_BITS = 12L;

    private static final long WORKER_ID_BITS = 10L;

    private static final long SEQUENCE_MASK = (1 << SEQUENCE_BITS ) - 1;

    private static final  long WORKER_ID_LEFT_SHIFT_BITS = SEQUENCE_BITS;

    private static final  long TIMESTAMP_LEFT_SHIFT_BITS = WORKER_ID_LEFT_SHIFT_BITS + WORKER_ID_BITS;

    private static final  long WORKER_ID_MAX_VALUE = 1L << WORKER_ID_BITS;

    private static final  long WORKER_ID = 0L;

    private static final int MAX_TOLERATE_TIME_DIFFERENCE_MILLISECONDS = 10;


    private SnowflakeShardingKeyGenerator (){

    }

    @Setter
    private static TimeService timeService = new TimeService();

    @Setter
    @Getter
    private Properties properties = new Properties();

    private byte  sequenceOffset;

    private long sequence;

    private long lastMilliseconds;

    static{
        Calendar calendar = Calendar.getInstance();
        calendar.set(2016,Calendar.NOVEMBER,1);
        calendar.set(Calendar.HOUR_OF_DAY,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        EPOCH = calendar.getTimeInMillis();
    }

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
        }else{
            vibrateSequenceOffset();
            sequence = sequenceOffset;
        }
        lastMilliseconds = currentMilliseconds;

        return ((currentMilliseconds - EPOCH) << TIMESTAMP_LEFT_SHIFT_BITS | (getWorkerId() << WORKER_ID_LEFT_SHIFT_BITS) |  sequence);
    }

    private long getWorkerId() {
        long result = Long.valueOf(properties.getProperty("worker.id",String.valueOf(WORKER_ID)));
        Preconditions.checkArgument(result >= 0 &&  result < WORKER_ID_MAX_VALUE);

        return result;
    }

    private void vibrateSequenceOffset() {

        sequenceOffset = (byte) (~sequenceOffset & 1);
    }

    private long waitUntilNextTime(long lastTime) {
        long result = timeService.getCurrentMillis();

        while (result <= lastTime) {
            result = timeService.getCurrentMillis();
        }

        return result;
    }

    @SneakyThrows
    private boolean waitTolerateTimeDifferenceIfNeed(long currentMilliseconds) {

        if (lastMilliseconds <= currentMilliseconds){
            return false;
        }

        long timeDifferenceMilliseconds = lastMilliseconds - currentMilliseconds;

        Preconditions.checkState(timeDifferenceMilliseconds <  getMaxTolerateTimeDifferenceMilliseconds(),
                "Clock is moving backwards  last  time is %d  milliseconds ,current time  is  %d  milliseconds",lastMilliseconds,currentMilliseconds );
        Thread.sleep(currentMilliseconds);
        return true;

    }

    private long getMaxTolerateTimeDifferenceMilliseconds() {

        return Integer.valueOf(properties.getProperty("max.tolerate.time.difference.milliseconds",
                String.valueOf(MAX_TOLERATE_TIME_DIFFERENCE_MILLISECONDS)));

    }
    
    /*** 
     * @description:  获取单列对象
     * @param 
     * @return  单列对象
     * @author Long
     * @date: 2022/9/4 0:27
     */ 
    public static SnowflakeShardingKeyGenerator getInstance(){

        if (instance == null){
            synchronized (SnowflakeShardingKeyGenerator.class){
                instance = new SnowflakeShardingKeyGenerator();
            }
        }
        return instance;

    }
}
