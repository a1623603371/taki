package com.taki.order.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName CheckLackDTO
 * @Description 效验缺品请求结果DTO
 * @Author Long
 * @Date 2022/3/9 15:24
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckLackDTO {

    private OrderInfoDTO orderInfo;

    private List<OrderLackItemDTO> orderLackItems;
}
