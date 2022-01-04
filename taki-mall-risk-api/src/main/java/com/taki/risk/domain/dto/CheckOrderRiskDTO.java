package com.taki.risk.domain.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @ClassName CheckOrderRisk
 * @Description 订单风控检查
 * @Author Long
 * @Date 2022/1/4 9:53
 * @Version 1.0
 */
@Data
public class CheckOrderRiskDTO extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -4785859201491023079L;

    private Boolean result;

    private List<String> noticeList;
}
