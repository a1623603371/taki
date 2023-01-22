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
 * 消息发送任务表
 * </p>
 *
 * @author long
 * @since 2022-10-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("push_message_crontab")
public class PushMessageCrontabDO extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 消息id
     */
    @TableField("message_id")
    private Long messageId;

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
     * 通知类型：1短信，2app通知，3email通知
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
     * 定时任务时间
     */
    @TableField("crontab_time")
    private LocalDateTime crontabTime;

    /**
     * 发送周期序号
     */
    @TableField("period_send_number")
    private Integer periodSendNumber;

    /**
     * 任务是否执行： 1-是，2-否
     */
    @TableField("execute_flag")
    private Integer executeFlag;

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


    public static final String MESSAGE_ID = "message_id";

    public static final String MAIN_MESSAGE = "main_message";

    public static final String PUSH_TYPE = "push_type";

    public static final String INFORM_TYPE = "inform_type";

    public static final String MESSAGE_INFO = "message_info";

    public static final String FILTER_CONDITION = "filter_condition";

    public static final String CRONTAB_TIME = "crontab_time";

    public static final String EXECUTE_FLAG = "execute_flag";

    public static final String CREATE_USER = "CREATE_USER";

    public static final String UPDATE_USER = "UPDATE_USER";

}
