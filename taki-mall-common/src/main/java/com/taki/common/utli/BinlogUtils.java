package com.taki.common.utli;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;

import com.taki.common.domin.dto.BinlogDataDTO;

import java.util.*;

/**
 * @ClassName BinlogUtils
 * @Description MySQL binlog解析类
 * @Author Long
 * @Date 2023/3/5 18:22
 * @Version 1.0
 */
public abstract class BinlogUtils {

    /*** 
     * @description: 解析binlog josn 字符串 （表数据已实体类Map的形式返回）
     * @param binlogStr
     * @return com.taki.common.domin.dto.BinlogDataDTO
     * @author Long
     * @date: 2023/3/5 18:24
     */
    public static BinlogDataDTO getBinlogData(String binlogStr) {

        //isJson方法里面会判断字符串是不是为空，所以这里不需要重复判断
        if (JSONUtil.isJson(binlogStr)) {

            JSONObject binlogDataJson = JSONUtil.parseObj(binlogStr);
            BinlogDataDTO.BinlogDataDTOBuilder build = BinlogDataDTO.builder();


            JSONArray dataArray = binlogDataJson.getJSONArray("data");
            BinlogDataDTO binlogData = build
                    .tableName(binlogDataJson.getStr("table"))
                    .operateType(binlogDataJson.getStr("type"))
                    .operateTime(binlogDataJson.getLong("ts"))
                    .build();
            if (Objects.nonNull(dataArray)) {
                Iterable<JSONObject> dataArrayIterable = dataArray.jsonIter();

                // 遍历data 节点并反射生成对象
                if (Objects.nonNull(dataArrayIterable)) {
                    //biglog的data数组里数据的类型为Map
                    List<Map<String, Object>> dataMap = new ArrayList<>();
                    while (dataArrayIterable.iterator().hasNext()) {
                        JSONObject jsonObject = dataArrayIterable.iterator().next();

                        Map<String, Object> data = new HashMap<>();
                        jsonObject.keySet().forEach(key -> {
                            String camelKey = StrUtil.toCamelCase(StrUtil.lowerFirst(key));
                            data.put(camelKey, jsonObject.get(key));
                        });
                        dataMap.add(data);

                    }
                    binlogData.setDataMap(dataMap);
                }
            }
            return binlogData;
        }
        return null;

    }

}
