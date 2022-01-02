package com.taki.message.domian.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import com.taki.common.domin.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author long
 * @since 2021-12-06
 */
@Getter
@Setter
@Accessors(chain = true)
@TableName("short_message_platform")
@ApiModel(value = "ShortMessagePlatformDO对象", description = "")
public class ShortMessagePlatformDO extends BaseEntity {

    private static final long serialVersionUID = 1L;



    @ApiModelProperty("平台名称")
    @TableField("platform_name")
    private String platformName;

    @ApiModelProperty("平台编号（自定义代码区别）")
    @TableField("platform_code")
    private String platformCode;

    @ApiModelProperty("密钥ID")
    @TableField("secret_id")
    private String secretId;

    @ApiModelProperty("密钥KEY")
    @TableField("secret_key")
    private String secretKey;

    @ApiModelProperty("appKey")
    @TableField("api_key")
    private String apiKey;

    @ApiModelProperty("SDK应用ID")
    @TableField("sdk_app_id")
    private String sdkAppId;

    @ApiModelProperty("签名")
    @TableField("sign")
    private String sign;

    @ApiModelProperty("模板id")
    @TableField("template_id")
    private String templateId;

    @ApiModelProperty("请求URL")
    @TableField("request_url")
    private String requestUrl;

    @ApiModelProperty("SDK 或API")
    @TableField("send_type")
    private String sendType;

    @ApiModelProperty("SECRET或APIKEY")
    @TableField("auth_type")
    private String authType;

    @ApiModelProperty("短信类型 REGISTER  注册, LOGIN 登录 ,MODIFY_PASS 修改密码 ，NOTICE 通知 OTHER 其他")
    @TableField("type")
    private String type;


    @ApiModelProperty("短信模板内容 和 短信平台模板对应")
    @TableField("template")
    private String template;

    @ApiModelProperty("是否开启 0 关闭， 1开启")
    @TableField("is_open")
    private Boolean open;




    public static final String ID = "id";

    public static final String PLATFORM_NAME = "platform_name";

    public static final String PLATFORM_CODE = "platform_code";

    public static final String SECRET_ID = "secret_id";

    public static final String SECRET_KEY = "secret_key";

    public static final String API_KEY = "api_key";

    public static final String SDK_APP_ID = "sdk_app_id";

    public static final String SIGN = "sign";

    public static final String TEMPLATE_ID = "template_id";

    public static final String REQUEST_URL = "request_url";

    public static final String SNED_TYPE = "sned_type";

    public static final String AUTH_TYPE = "auth_type";

    public static final  String TYPE = "type";

    public static final String IS_OPEN = "is_open";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";


}
