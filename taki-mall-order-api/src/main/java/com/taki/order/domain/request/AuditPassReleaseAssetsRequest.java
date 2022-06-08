package com.taki.order.domain.request;

import com.taki.common.core.AbstractObject;
import com.taki.common.message.ActualRefundMessage;
import com.taki.order.domain.dto.ReleaseProductStockDTO;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName AuditPassReleaseAssetsRequest
 * @Description 接收客服审核通过后 的 监听 释放资产 请求
 * @Author Long
 * @Date 2022/5/13 16:02
 * @Version 1.0
 */
@Data
public class AuditPassReleaseAssetsRequest extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -8581565465136954245L;


    /**
     *释放库存DTO
     */
    private ReleaseProductStockDTO releaseProductStockDTO;


    /**
     * 实际退款message数据
     */
    private ActualRefundMessage actualRefundMessage;
}
