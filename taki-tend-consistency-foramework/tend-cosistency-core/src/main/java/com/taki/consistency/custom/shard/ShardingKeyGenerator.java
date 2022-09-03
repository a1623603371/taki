package com.taki.consistency.custom.shard;

/**
 * @ClassName ShardingKeyGenerator
 * @Description 任务分片键生成器接口
 * 如果业务服务需要定制，实现该接口
 * @Author Long
 * @Date 2022/9/3 20:51
 * @Version 1.0
 */
public interface ShardingKeyGenerator {

    /*** 
     * @description:  生产一致任务分片键
     * @param 
     * @return 一致任务分片键
     * @author Long
     * @date: 2022/9/3 20:52
     */ 
    Long generateShardKey();
}
