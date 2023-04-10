package com.taki.elasitcserach.domain.dto;

import lombok.Data;

/**
 * @ClassName MockData2DTO
 * @Description TODO
 * @Author Long
 * @Date 2023/3/21 17:22
 * @Version 1.0
 */
@Data
public class MockData3DTO {

    /**
     * 索引名称
     */
    private String indexName;

    /**
     * 一次批量插入的文档数据
     */
    private int batchSize;

    /**
     * 执行批量 插入次数
     */
    private int batchTimes;



    public boolean validateParams(){
        if (indexName == null || indexName.trim().length() == 0){
            return false;
        }

        if (batchSize <= 0 || batchTimes <= 0 ){
            return false;
        }

        return true;
    }
}
