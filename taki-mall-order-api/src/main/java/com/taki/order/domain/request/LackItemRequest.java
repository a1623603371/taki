package com.taki.order.domain.request;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

/**
 * @ClassName LackItemRequest
 * @Description 缺品条目请求
 * @Author Long
 * @Date 2022/3/3 21:40
 * @Version 1.0
 */
@Data
public class LackItemRequest  implements Serializable {


    private static final long serialVersionUID = -8054914018927686350L;

    /**
     * sku 编码
     */
    private String skuCode;

    /**
     * 缺品数量
     */
    private Integer lackNum;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LackItemRequest that = (LackItemRequest) o;
        return Objects.equals(skuCode, that.skuCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(skuCode);
    }
}
