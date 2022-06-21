package com.taki.inventory.domain.request;

import lombok.Data;

import java.util.List;

/**
 * @ClassName InitMeasureDataRequest
 * @Description 数据初始化
 * @Author Long
 * @Date 2022/6/20 16:38
 * @Version 1.0
 */
@Data
public class InitMeasureDataRequest {

    private List<String> skuCodes;
}
