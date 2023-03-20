package com.taki.common.enums;

import com.taki.common.constants.RedisCacheKey;
import com.taki.common.redis.RedisCache;
import lombok.Getter;

import java.util.Arrays;

/**
 * @ClassName ConsistencyTableEum
 * @Description 存储关于配置的目前需要支持的缓存一致性的表相关信息
 * @Author Long
 * @Date 2023/3/5 19:10
 * @Version 1.0
 */
@Getter
 public  enum ConsistencyTableEnum {
    /**
     * 商品表缓存配置
     */
    SKU_INFO("sku_info", RedisCacheKey.GOODS_INFO_PREFIX,"id") ;

    /**
     * 配置相关表名称
     */
    private final String tableName;

    /**
     *  缓存的前缀key
     */
    private final String cacheKey;


    /**
     *缓存标识字段
     */
    private final  String cacheField;


    ConsistencyTableEnum(String tableName, String cacheKey, String cacheField) {
        this.tableName = tableName;
        this.cacheKey = cacheKey;
        this.cacheField = cacheField;
    }


    /**
     * 根据枚举类型的值
     * @param tableName
     * @return
     */
    public static  ConsistencyTableEnum findByEnum(String tableName){

        for (ConsistencyTableEnum consistencyTable : values()) {

            if (consistencyTable.getTableName().equals(tableName)){
                return consistencyTable;
            }
        }
        return null;

    }
}
