package com.taki.order.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName RevokeAfterSaleRequest
 * @Description 用户撤销售后请求
 * @Author Long
 * @Date 2022/3/9 15:04
 * @Version 1.0
 */
@Data
public class RevokeAfterSaleRequest extends AbstractObject implements Serializable {

    /**
     * 售后Id
     */
    private String afterSaleId;
}
