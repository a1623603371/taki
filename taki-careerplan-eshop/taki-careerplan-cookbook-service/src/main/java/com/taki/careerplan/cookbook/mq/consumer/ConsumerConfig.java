package com.taki.careerplan.cookbook.mq.consumer;

import com.taki.careerplan.cookbook.mq.config.RocketMQProperties;
import com.taki.careerplan.cookbook.mq.consumer.listener.CookbookConsistencyListener;
import com.taki.careerplan.cookbook.mq.consumer.listener.CookbookUpdateListener;
import com.taki.common.constants.RocketMQConstant;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.exception.MQClientException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName ConsumerConfig
 * @Description TODO
 * @Author Long
 * @Date 2023/3/5 19:53
 * @Version 1.0
 */
@Configuration
public class ConsumerConfig {


    /**
     * 配置内容对象
     */
    @Autowired
    private RocketMQProperties rocketMQProperties;

    /*** 
     * @description: 购物车商品更新消费者
     * @param cookbookUpdateListener
     * @return  org.apache.rocketmq.client.consumer.DefaultMQPushConsumer
     * @author Long
     * @date: 2023/3/5 19:55
     */ 
    @Bean("cookbookCartAsyncUpdateTopic")
    public DefaultMQPushConsumer cookbookCartAsyncUpdateTopic (CookbookUpdateListener cookbookUpdateListener) throws MQClientException {

        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQConstant.COOKBOOK_DEFAULT_CONSUMER_GROUP);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(RocketMQConstant.COOKBOOK_UPDATE_MESSAGE_TOPIC,"*");
        consumer.registerMessageListener(cookbookUpdateListener);
        consumer.start();
        return consumer;
    }

    /*** 
     * @description: 数据变更监听binlog 消费者
     * @param cookbookConsistencyListener
     * @return  org.apache.rocketmq.client.consumer.DefaultMQPushConsumer
     * @author Long
     * @date: 2023/3/5 20:00
     */ 
    @Bean("cookbookConsistencyTopic")
    public  DefaultMQPushConsumer cookbookConsistencyTopic (CookbookConsistencyListener cookbookConsistencyListener) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(RocketMQConstant.BINLOG_DEFAULT_CONSISTENCY_GROUP);
        consumer.setNamesrvAddr(rocketMQProperties.getNameServer());
        consumer.subscribe(RocketMQConstant.BINLOG_DEFAULT_CONSISTENCY_TOPIC,"*");
        consumer.registerMessageListener(cookbookConsistencyListener);
        consumer.start();
        return consumer;
        
    }
}
