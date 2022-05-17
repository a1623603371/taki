package com.taki.tms.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName PlaceLogisticOrderDTO
 * @Description TODO
 * @Author Long
 * @Date 2022/5/17 15:02
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlaceLogisticOrderDTO {

    /**
     * 三方物流单号
     */
    private String logisticCode;

    /**
     * 物流 单内容
     */
    private String content;
}
