package com.taki.market.mq.event;

import com.taki.market.domain.entity.MarketPromotionDO;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName SalesPromotionCreatedEvent
 * @Description 查询活动创建事件
 * @Author Long
 * @Date 2022/10/3 23:32
 * @Version 1.0
 */
@Data
public class SalesPromotionCreatedEvent implements Serializable {

    private MarketPromotionDO marketPromotion;
}
