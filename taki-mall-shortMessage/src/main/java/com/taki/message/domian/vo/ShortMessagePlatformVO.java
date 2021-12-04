package com.taki.message.domian.vo;


import lombok.Data;
import lombok.experimental.Accessors;

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
public class ShortMessagePlatformVO {



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
