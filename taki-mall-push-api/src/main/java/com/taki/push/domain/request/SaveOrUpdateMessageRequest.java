package com.taki.push.domain.request;

import com.taki.user.domain.dto.MemberFilterDTO;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName SaveOrUpdateMessageRequest
 * @Description 消息推送请求
 * @Author Long
 * @Date 2022/10/5 15:52
 * @Version 1.0
 */
@Data
@Builder
public class SaveOrUpdateMessageRequest {

    /**
     * 推送类型
     */
    private Integer pushType;

    /**
     * 消息主题
     */
    private String mainMessage;

    /**
     * 1.短信 ， 2。app消息 ，3 邮箱
     */
    private Integer informType;

    /**
     * 消息
     */
    private String message;

    /**
     * 挑选添加
     *
     */
    private MemberFilterDTO memberFilterDTO;


    /**
     * 定时发送任务开始时间
     */
    private LocalDateTime pushStartTime;


    /**
     * 定时发送任务结束时间
     */
    private LocalDateTime pushEndTime;


    /**
     * 每个发送周期内的发送次数，以此为依据发送消息
     */
    private Integer sendPeriodCount;

    /**
     * 推送消息创建/修改人
     */
    private Integer createUser;
}
