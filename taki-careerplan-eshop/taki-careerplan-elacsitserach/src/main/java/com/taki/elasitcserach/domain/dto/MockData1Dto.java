package com.taki.elasitcserach.domain.dto;

import lombok.Data;

/**
 * @ClassName MockData1Dto
 * @Description TODO
 * @Author Long
 * @Date 2023/3/20 23:17
 * @Version 1.0
 */
@Data
public class MockData1Dto {

    /**
     * 索引名称
     */
    private String indexName;

    /**
     * 一次性批量插入的文档数
     */
    private int batchSize;

    /**
     * 执行批量插入次数
     */
    private int batchTimes;

    public boolean validateParams(){
        if (indexName == null || indexName.trim().length() == 0){
            return false;
        }

        if (batchSize <=0 ||  batchTimes <= 0 ){

            return false;
        }

        return  true;

    }
}
