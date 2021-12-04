package com.taki.message.domian.dto;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;


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

public class ShortMessagePlatformDTO  {



    private Long id;


    private String platformName;


    private String platformCode;


    private String secretId;


    private String secretKey;


    private String apiKey;


    private String snedType;


    private String authType;


    private Boolean open;


    private LocalDateTime createTime;


    private LocalDateTime updateTime;



}
