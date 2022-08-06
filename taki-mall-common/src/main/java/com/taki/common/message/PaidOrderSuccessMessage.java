package com.taki.common.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName PaidOrderSuccessMessage
 * @Description 支付完成支付消息
 * @Author Long
 * @Date 2022/1/17 17:15
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaidOrderSuccessMessage implements Serializable {


    private static final long serialVersionUID = -6610656630007354754L;

    private String orderId;
}
