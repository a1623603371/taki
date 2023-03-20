package com.taki.common.redis;


import com.jd.platform.hotkey.client.callback.JdHotKeyStore;
import com.taki.common.utli.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.script.RedisScript;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName RedisCache
 * @Description redis缓存
 * @Author Long
 * @Date 2021/12/20 11:30
 * @Version 1.0
 */
@Slf4j
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
    
    /*** 
     * @description:  缓存获取 首先 从内存中获取，在从缓存中缓存
     * @param key
     * @return  java.lang.Object
     * @author Long
     * @date: 2023/2/20 17:48
     */ 
    public Object getCache(String key) {


        /*
         * 从内存中获取商品信息
         * 如果是热key，则存在两种情况，1是返回value，2是返回null。
         * 返回null是因为尚未给它set真正的value，返回非null说明已经调用过set方法了，本地缓存value有值了。
         * 如果不是热key，则返回null，并且将key上报到探测集群进行数量探测。
         */
        Object hotKeyValue  = JdHotKeyStore.getValue(key);

        if (Objects.nonNull(hotKeyValue)){
            log.info("从内存中获取信息：key:{},value:{}",key,hotKeyValue);
            return hotKeyValue;
        }
        String value = this.get(key);

        if (Objects.nonNull(value)){
            log.info("从缓存中获取信息：key:{},value:{}",key,hotKeyValue);
        }
        /*
         * 方法给热key赋值value，如果是热key，该方法才会赋值，非热key，什么也不做
         * 如果是热key，存储在内存中
         * 每次从缓存中获取数据后都尝试往热key中放一下
         * 如果不放，则在成为热key之前，将数据放入缓存中了，但是没放到内存中。
         * 如果此时变成热key了，但是下次查询内存没查到，查缓存信息，查到了，就直接返回了，内存中就没有数据
         */
        JdHotKeyStore.smartSet(key, value);
        return value;

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


    /***
     * @description:  缓存增量
     * @param userCookbookCountKey
     * @param
     * @return  void
     * @author Long
     * @date: 2023/2/20 17:28
     */ 
    public void increment(String key, int increment) {
        redisTemplate.opsForValue().increment(key,increment);
    }

    /***
     * @description: 缓存减量
     * @param key
     * @return  void
     * @author Long
     * @date: 2023/2/20 17:30
     */
    public void decrement(String key,Integer decrement){
        redisTemplate.opsForValue().decrement(key,decrement);
    }

   
}
