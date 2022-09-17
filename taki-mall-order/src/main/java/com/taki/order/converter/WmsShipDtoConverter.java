package com.taki.order.converter;

import com.taki.fulfill.domain.evnet.OrderDeliveredWmsEvent;
import com.taki.fulfill.domain.evnet.OrderOutStockWmsEvent;
import com.taki.fulfill.domain.evnet.OrderSignedWmsEvent;
import com.taki.order.domain.dto.WmsShipDTO;
import org.mapstruct.Mapper;

/**
 * @author long
 * @version 1.0
 */
@Mapper(componentModel = "spring")
public interface WmsShipDtoConverter {

    /**
     * 对象转换
     *
     * @param event 对象
     * @return 对象
     */
    WmsShipDTO convert(OrderOutStockWmsEvent event);

    /**
     * 对象转换
     *
     * @param event 对象
     * @return 对象
     */
    WmsShipDTO convert(OrderDeliveredWmsEvent event);

    /**
     * 对象转换
     *
     * @param event 对象
     * @return 对象
     */
    WmsShipDTO convert(OrderSignedWmsEvent event);

}
