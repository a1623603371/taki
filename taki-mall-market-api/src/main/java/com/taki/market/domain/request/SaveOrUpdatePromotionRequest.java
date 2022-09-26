package com.taki.market.domain.request;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

/**
 * @ClassName SaveOrUpdatePromotionRequest
 * @Description 创建或更新促销活动
 * @Author Long
 * @Date 2022/9/25 21:12
 * @Version 1.0
 */
@Data
public class SaveOrUpdatePromotionRequest  implements Serializable {


    private static final long serialVersionUID = 5863232021070578104L;
    /**
     * 促销活动名称
     */
    private String name;

    /**
     * 1.短信 2.app消息 3.邮箱
     */
    private Integer informType;

    /**
     * 开始数据
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 活动状态 1，启动，2，停用
     */
    private Integer status;

    /**
     * 活动类型 1，满减 ，2折扣 ，3 优惠券 ， 4 会员积分
     */
    private Integer type;


    /**
     * 活动规则
     */
    private PromotionRulesValue rule;

    @Data
  public static  class PromotionRulesValue  implements Serializable  {


      private static final long serialVersionUID = -3468574103376080892L;
      /**
       * 规则名：1：满减 2：折扣 3.优惠券 4.会员积分
        */
    private String key;

      /**
       * 规则值，其中key为条件，value为活动规则值。
       * 例如：满减规则，200，30，代表满500，减30
       * 折扣规则，200，0.95，代表满500，打0.95折
       * 优惠券，500，10，代表满500，送一张10元优惠券
       * 会员积分，1000，100，代表满1000，额外送100积分
       */
    private Map<String,String> value;
  }
}
