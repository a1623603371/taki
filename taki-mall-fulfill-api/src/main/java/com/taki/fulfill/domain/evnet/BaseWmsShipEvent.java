package com.taki.fulfill.domain.evnet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @ClassName BaseWmsShipEvent
 * @Description TODO
 * @Author Long
 * @Date 2022/3/4 11:53
 * @Version 1.0
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseWmsShipEvent implements   Serializable {


    private static final long serialVersionUID = 3159934178756997759L;

    private String orderId;
}
