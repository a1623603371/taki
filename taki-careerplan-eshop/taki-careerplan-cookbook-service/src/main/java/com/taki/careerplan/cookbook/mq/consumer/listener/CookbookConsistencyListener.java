package com.taki.careerplan.cookbook.mq.consumer.listener;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.taki.common.domin.dto.BinlogDataDTO;
import com.taki.common.enums.BinlogTypeEnum;
import com.taki.common.enums.ConsistencyTableEnum;
import com.taki.common.mq.AbstractMessageListenerConcurrently;
import com.taki.common.redis.RedisCache;
import com.taki.common.utli.BinlogUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.common.message.MessageExt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @ClassName CookbookConsistencyListener
 * @Description 处理数据一致性的消息队列
 * @Author Long
 * @Date 2023/3/5 18:12
 * @Version 1.0
 */
@Component
@Slf4j
public class CookbookConsistencyListener extends AbstractMessageListenerConcurrently {

    @Autowired
    private RedisCache redisCache;

    @Override
    protected ConsumeConcurrentlyStatus omMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext consumeConcurrentlyContext) {

        try {
            msgs.forEach(msg->{
            String message = new String(msg.getBody());
            // 解析binlog数据模型，并过滤查询
            BinlogDataDTO binlogData = buildBinlogData(message);
            // 获取binlog的模型，获取本次变化的表名称，在本地配置常量类里面匹配对应的缓存key前缀以及缓存标识字段，非配置的表不进行处理
            String cacheKey =filterConsistencyTable(binlogData);
            // 删除改key的缓存
            deleteCacheKey(cacheKey);
            });
        }catch (Exception e){
            log.error("consumer  error,缓存清理失败",e);

            return ConsumeConcurrentlyStatus.RECONSUME_LATER;
        }
        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
    }

    /**
     * 对缓存进行清理
     * @param cacheKey
     */
    private void deleteCacheKey(String cacheKey) {
        if(StringUtils.isBlank(cacheKey)){
            return;
        }
        redisCache.delete(cacheKey);


    }

    /*** 
     * @description: 过滤掉目前不需要处理的表的Binlog，并返回所需的key
     * @param binlogData
     * @return  java.lang.String
     * @author Long
     * @date: 2023/3/5 19:02
     */ 
    private String filterConsistencyTable(BinlogDataDTO binlogData) {

        if (Objects.isNull(binlogData)){
            return null;
        }

        String tableName = binlogData.getTableName();

        List<Map<String,Object>> dataList = binlogData.getDataMap();

        // 获取配置常量映射的具体配置
        ConsistencyTableEnum consistencyTableEnum = ConsistencyTableEnum.findByEnum(tableName);

        if (Objects.isNull(consistencyTableEnum)){
            return null;
        }

        String cacheValue = "";
        if(CollectionUtils.isNotEmpty(dataList)){
            Map<String,Object> dataMap = dataList.get(0);

            cacheValue = dataMap.get(consistencyTableEnum.getCacheField()) + "";

        }

        if (StringUtils.isBlank(cacheValue)){
            return null;
        }

        // 获取配置 缓存前缀key +当前标识字段 组件缓存key
        return consistencyTableEnum.getCacheKey() + cacheValue;


    }

    /*** 
     * @description: 解析binlog的数据模型，并过滤掉 查询Binlog
     * @param msg
     * @return  com.taki.common.domin.dto.BinlogDataDTO
     * @author Long
     * @date: 2023/3/5 18:22
     */ 
    private BinlogDataDTO buildBinlogData(String msg) {
        //先解析binlog的对象，转换为模型
        BinlogDataDTO binlogData = BinlogUtils.getBinlogData(msg);
        //模型为null则直接返回
        if(Objects.isNull(binlogData)){
            return null;
        }
        Boolean isOperateType = BinlogTypeEnum.INSERT.getValue().equals(binlogData.getOperateType()) ||
                BinlogTypeEnum.UPDATE.getValue().equals(binlogData.getOperateType()) ||
                BinlogTypeEnum.DELETE.getValue().equals(binlogData.getOperateType());

        if (!isOperateType || CollectionUtils.isEmpty(binlogData.getDataMap())){
            return null;
        }

        /**
         * 返回解析好的可用模型
         */
        return binlogData;
    }
}
