package com.taki.common.redis;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;
import java.util.Map;

/**
 * @ClassName RedisCache
 * @Description redis缓存
 * @Author Long
 * @Date 2021/12/20 11:30
 * @Version 1.0
 */

public class RedisCache {

    private RedisTemplate redisTemplate;

    public RedisCache(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * @description: 将数据存储到redis
     * @param key  键值
     * @param value 数据
     * @param seconds 过期秒数
      * @return  void
     * @author Long
     * @date: 2021/12/20 11:33
     */
    public void set(String key,String value,int seconds){

        ValueOperations<String,String> valueOperations =   redisTemplate.opsForValue();

        if (seconds > 0){
            valueOperations.set(key,value,seconds);
        }else {
            valueOperations.set(key,value);
        }

    }

    /**
     * @description: 获取缓存中的数据
     * @param key 键值
     * @return  缓存数据
     * @author Long
     * @date: 2021/12/20 11:39
     */
    public String get(String key){
        ValueOperations<String,String> valueOperations =   redisTemplate.opsForValue();
        return valueOperations.get(key);
    }

    /**
     * @description: 删除缓存中的数据
     * @param key 键值
     * @return  缓存数据
     * @author Long
     * @date: 2021/12/20 11:39
     */
    public boolean delete(String key){

         return redisTemplate.delete(key);
    }

    /***
     * @description: 判断 key 是否存在
     * @param key
     * @return  boolean
     * @author Long
     * @date: 2022/5/11 14:51
     */
    public boolean hExists(String key){

        return  hGetAll(key).isEmpty();
    }


    /**
     * 获取 hash变量中 的key 和value
     * 对应redis hgetall 命令
     * @param key
     * @return
     */
    public Map<String,String> hGetAll(String key){
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 已map 集合形式 添加 hashmap
     * @param key
     * @param map
     * @return
     */
    public void hPutAll(String key,Map<String,String> map){

         redisTemplate.opsForHash().putAll(key,map);
    }



    /*** 
     * @description: 执行lua脚本
     * @param script lua 脚本
     * @param keys 键值
     * @param args
     * @return  T
     * @author Long
     * @date: 2022/5/11 14:58
     */ 
    public <T> T execute(RedisScript<T> script, List<String> keys,String ... args){

        return(T) redisTemplate.execute(script,keys,args);
    }
}
