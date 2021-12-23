//package com.taki.common.redis;
//
//
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//
///**
// * @ClassName RedisCache
// * @Description redis缓存
// * @Author Long
// * @Date 2021/12/20 11:30
// * @Version 1.0
// */
//
//public class RedisCache {
//
//    private RedisTemplate redisTemplate;
//
//    public RedisCache(RedisTemplate<String, Object> redisTemplate) {
//        this.redisTemplate = redisTemplate;
//    }
//
//    /**
//     * @description: 将数据存储到redis
//     * @param key  键值
//     * @param value 数据
//     * @param seconds 过期秒数
//      * @return  void
//     * @author Long
//     * @date: 2021/12/20 11:33
//     */
//    public void set(String key,String value,int seconds){
//
//        ValueOperations<String,String> valueOperations =   redisTemplate.opsForValue();
//
//        if (seconds > 0){
//            valueOperations.set(key,value,seconds);
//        }else {
//            valueOperations.set(key,value);
//        }
//
//    }
//
//    /**
//     * @description: 获取缓存中的数据
//     * @param key 键值
//     * @return  缓存数据
//     * @author Long
//     * @date: 2021/12/20 11:39
//     */
//    public String get(String key){
//        ValueOperations<String,String> valueOperations =   redisTemplate.opsForValue();
//        return valueOperations.get(key);
//    }
//
//    /**
//     * @description: 删除缓存中的数据
//     * @param key 键值
//     * @return  缓存数据
//     * @author Long
//     * @date: 2021/12/20 11:39
//     */
//    public boolean delete(String key){
//
//         return redisTemplate.delete(key);
//    }
//}
