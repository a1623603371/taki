package com.taki.push.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.domin.BaseEntity;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 优惠券表
 * </p>
 *
 * @author long
 * @since 2022-10-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("push_message")
public class PushMessageDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 消息摘要
     */
    @TableField("main_message")
    private String mainMessage;

    /**
     * 推送类型：1定时推送，2直接推送
     */
    @TableField("push_type")
    private Integer pushType;

    /**
     * 通知类型：1：短信，2：app消息，3：邮箱
     */
    @TableField("inform_type")
    private Integer informType;

    /**
     * 消息内容
     */
    @TableField("message_info")
    private String messageInfo;

    /**
     * 筛选条件
     */
    @TableField("filter_condition")
    private String filterCondition;

    /**
     * 定时发送消息任务开始时间
     */
    @TableField("push_start_time")
    private LocalDateTime pushStartTime;

    /**
     * 定时发送消息任务结束时间
     */
    @TableField("push_end_time")
    private LocalDateTime pushEndTime;

    /**
     * 周期内推送次数
     */
    @TableField("send_period_count")
    private Integer sendPeriodCount;

    /**
     * 创建人
     */
    @TableField("CREATE_USER")
    private Integer createUser;

    /**
     * 更新人
     */
    @TableField("UPDATE_USER")
    private Integer updateUser;


    public static final String MAIN_MESSAGE = "main_message";

    public static final String PUSH_TYPE = "push_type";

    public static final String INFORM_TYPE = "inform_type";

    public static final String MESSAGE_INFO = "message_info";

    public static final String FILTER_CONDITION = "filter_condition";

    public static final String PUSH_START_TIME = "push_start_time";

    public static final String PUSH_END_TIME = "push_end_time";

    public static final String SEND_PERIOD_COUNT = "send_period_count";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String UPDATE_USER = "UPDATE_USER";

}
