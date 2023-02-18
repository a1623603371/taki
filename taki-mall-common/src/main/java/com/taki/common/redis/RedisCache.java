package com.taki.common.redis;


import com.jd.platform.hotkey.client.callback.JdHotKeyStore;
import com.taki.common.utli.JsonUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

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

    /*** 
     * @description: 缓存存储
     *  间数据存储到内存和 redis 中 ，如果不是热key， 就只存储 redis
     * @param key
     * @param value
     * @param seconds
     * @return  void
     * @author Long
     * @date: 2023/2/18 18:40
     */ 
    public void setCache(String key,Object value,int seconds){

        /*
         * 方法 给 热 key 赋值 value ，如果是热key，该方法才会赋值，非热key，什么都不做
         * 如果热key，存储在内存中
         */
        JdHotKeyStore.smartSet(key,value);

        this.set(key, JsonUtil.object2Json(value),seconds);


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
    
    /*** 
     * @description: 缓存失效时间
     * @param key 缓存key
     * @param seconds 时间单位
     * @return  java.lang.Long
     * @author Long
     * @date: 2023/2/18 21:36
     */ 
    public Long getExpire(String key, TimeUnit seconds) {

        return redisTemplate.getExpire(key,seconds);
    }

    /***
     * @description:  设置缓存时间
     * @param key 缓存key
     * @param second 时间单位秒数
     * @return  void
     * @author Long
     * @date: 2023/2/18 21:42
     */
    public void expire(String key, long second) {
        redisTemplate.expire(key,second,TimeUnit.SECONDS);
    }
}
