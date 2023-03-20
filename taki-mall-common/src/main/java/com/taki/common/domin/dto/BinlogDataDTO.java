package com.taki.common.domin.dto;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @ClassName BinlogDataDTO
 * @Description TODO
 * @Author Long
 * @Date 2023/3/5 18:15
 * @Version 1.0
 */
@Data
@Builder
public class BinlogDataDTO implements Serializable {


    private static final long serialVersionUID = -5699249035031223363L;


    /**
     * binlog对应的 表名称
     */
    private String tableName;


    /**
     * 操作时间
     */
    private Long operateTime;


    /**
     * 操作类型
     */
    private String operateType;


    /**
     * data节点转换成map key对应的是bean里的属性名，value一律为字符串（它和datas只有自会有一个值）
     */
    private List<Map<String,Object>> dataMap;


    /**
     * data 节点转换的bean （它和dataMap只有一个值）
     */
    private List<Object> datas;

    @Override
    public String toString() {
        return new StringJoiner(", ", BinlogDataDTO.class.getSimpleName() + "[", "]")
                .add("tableName=" + tableName)
                .add("operateTime=" + operateTime)
                .add("operateType='" + operateType + "'")
                .add("dataMap=" + dataMap)
                .add("datas=" + datas)
                .toString();
    }
}
