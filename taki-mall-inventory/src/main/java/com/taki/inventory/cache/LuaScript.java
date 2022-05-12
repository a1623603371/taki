package com.taki.inventory.cache;

/**
 * @ClassName LuaScript
 * @Description lua 脚本
 * @Author Long
 * @Date 2022/5/10 18:44
 * @Version 1.0
 */
public interface LuaScript {

    /**
     * 调整 商品库存
     */
    String MODIFY_PRODUCT_STOCK =
            "local productStockKey = KEYS[1];" +
            "local saleStockKey = KEYS[2];" +
            "local originSaleStockQuantity = tonumber(ARGV(1));" +
            "local stockIncremental = tonumber(ARGV(2));" +
            "local saleStock = tonumber(redis.call('hget',productStockKey,saleStockKey));" +
            "if saleStock ~= originSaleStockQuantity then " +
             "  return -1;" +
             "end;" +
             "if(saleStock+stockIncremental) < 0 then" +
             "  return -1;" +
             "end;" +
             "redis.call('hst',productStockKey,saleStockKey,saleStock + stockIncremental);" +
             "return 1";
            ;

    /**
     *扣减商品库存
     */
    String DEDUCT_PRODUCT_STOCK =
            "local productStockKey = KEYS[1];" +
            "local saleStockKey = KEYS[2];" +
            "local saledStockKey = KEYS[3];" +
            "local saleQuantity = tonumber(ARGV([1]);" +
            "local saleStock = tonumber(redis.call('hget',productStockKey,saleStockKey));" +
            "local saledStock = tonumber(redis.call('hget',productStockKey,saledStockKey))" +
            "if  saleStock < saleQuantity then " +
            "   return -1;" +
            "end;" +
            "redis.call('hset',productStockKey,saleStockKey, saleStock - saleQuantity)" +
            "redis.call('hset',productStockKey,saledStockKey,saledStock + saleQuantity)" +
            "return 1;";

    /**
     * 扣减销售库存
     */
    String DEDUCT_SALE_STOCK =
            "local productStockKey = KEYS[1];" +
            "local saleStockKey = KEYS[2];" +
            "local saleQuantity = tonumber(ARGV[1]);" +
            "local originSaleStock = tonumer(ARGV[2]);" +
            "local saleStock = tonumer(redis.call('hget',productStockKey,saleStockKey));" +
            "if saleStock < saleQuantity then " +
            "   return -1;" +
            "end;" +
            "if saleStock ~= originSaleStock then" +
            "   return -1;" +
            "end" +
            "redis.call('hset',productStockKey,saleStockKey,saleStock - saleQuantity)" +
            "return 1;";

    /**
     * 增加已销售库存
     */
    String INCREASE_SALED_STOCK =
            "local productStockKey = KEYS[1];" +
            "local saledStockKey = KEYS[2];" +
            "local saleQuantity = tonumber(ARGV[1]);" +
            "local originSaledStock = tonumber(ARGV[2]);" +
            "local saledStock = tonumber(redis.call('hget',productStockKey,saledStockKey))" +
            "if saleStock ~= originSaledStock then" +
            "   return -1;" +
            "end;" +
            "redis.call('hset',productStockKey,saledStockKey,saledStock + saleQuantity)" +
            "return 1;";

    /**
     * 释放销售库存
     */
    String RELEASE_SALE_STOCK =
            "local productStockKey = KEYS[1];" +
            "local saleStockKey = KEYS[2];" +
            "local saleQuantity = tonumber(ARGV(1));" +
            "local originSaleStock = tonumer(ARGV(2));" +
            "local saleStock = tonumer(redis.call('hget',productStockKey,saleStockKey))" +
            "if saleStock ~= originSaleStock then" +
            "   return -1;" +
            "end;" +
            "redis.call('hset',productStockKey,saleStockKey,saleStock + saleQuantity)" +
            "return 1;";


    /**
     *
     * 恢复已售库存
     */
    String RELEASE_PRODUCT_STOCK =
            "local productStockKey =KEYS[1];" +
            "local saleStockKey =KEYS[2];" +
            "local saledStockKey =  KEYS[3];" +
            "local saleQuantity = tonumer(ARGV(1));" +
            "local saleStock = tonumer(redis.call('hget',prodcutStockKey,saleStockKey))" +
            "local saledStock = tonumer(redis.call('hget',productStockKey,saledStockKey));" +
            "if saledStock < saleQuantity then" +
            "   return -1;" +
            "end;" +
            "redis.call('hset',productStockKey,saleStockKey,saleStock + saleQuantity);" +
            "redis.call('hset',productStockKey,saledStockKey,saledStock - saleQuantity);" +
            "return 1;";
}
