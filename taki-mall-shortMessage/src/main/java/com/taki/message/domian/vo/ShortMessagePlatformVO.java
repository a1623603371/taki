package com.taki.message.domian.vo;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

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
public class ShortMessagePlatformVO {




    private Long id;

    @ApiModelProperty("平台名称")
    private String platformName;

    @ApiModelProperty("平台编号（自定义代码区别）")
    private String platformCode;

    @ApiModelProperty("密钥ID")
    private String secretId;

    @ApiModelProperty("密钥KEY")
    private String secretKey;

    @ApiModelProperty("appKey")
    private String apiKey;

    @ApiModelProperty("SDK应用ID")
    private String sdkAppId;

    @ApiModelProperty("签名")
    private String sign;

    @ApiModelProperty("模板id")
    private String templateId;

    @ApiModelProperty("请求URL")
    private String requestUrl;

    @ApiModelProperty("SDK 或API")
    private String sendType;

    @ApiModelProperty("SECRET或APIKEY")
    private String authType;

    @ApiModelProperty("短信类型 REGISTER  注册, LOGIN 登录 ,MODIFY_PASS 修改密码 ，NOTICE 通知 OTHER 其他")
    private String type;

    @ApiModelProperty("短信模板内容 和 短信平台模板对应")
    private String template;
    @ApiModelProperty("是否开启 0 关闭， 1开启")
    private Boolean open;

    @ApiModelProperty("创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    private LocalDateTime updateTime;



}
