package com.taki.message.domian.dto;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.taki.common.core.AbstractObject;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;


/**
 * <p>
 *  短信平台实体类
 * </p>
 *
 * @author long
 * @since 2021-12-04
 */
@Data
@Accessors(chain = true)
public class ShortMessagePlatformDTO extends AbstractObject implements Serializable {



    private Long id;


    private String platformName;


    private String platformCode;


    private String secretId;


    private String secretKey;

    private String apiKey;

    private String sdkAppId;


    private String sign;


    private String templateId;


    private String requestUrl;


    private String sendType;


    private String authType;


    private String type;

    private String template;

    private Boolean open;


    private LocalDateTime createTime;


    private LocalDateTime updateTime;



}
