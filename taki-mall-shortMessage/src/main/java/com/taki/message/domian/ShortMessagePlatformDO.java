package com.taki.message.domian;


import java.io.Serializable;
import java.time.LocalDateTime;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;
import lombok.Data;
import lombok.experimental.Accessors;


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
@TableName("short_message_platform")
@ApiModel(value = "ShortMessagePlatformDO对象", description = "")
public class ShortMessagePlatformDO extends Model<ShortMessagePlatformDO> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

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

    @ApiModelProperty("SDK 或API")
    @TableField("sned_type")
    private String snedType;

    @ApiModelProperty("SECRET或APIKEY")
    @TableField("auth_type")
    private String authType;

    @ApiModelProperty("是否开启 0 关闭， 1开启")
    @TableField("is_open")
    private Boolean open;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time",fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @ApiModelProperty("修改时间")
    @TableField(value = "update_time",fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;


    public static final String ID = "id";

    public static final String PLATFORM_NAME = "platform_name";

    public static final String PLATFORM_CODE = "platform_code";

    public static final String SECRET_ID = "secret_id";

    public static final String SECRET_KEY = "secret_key";

    public static final String API_KEY = "api_key";

    public static final String SNED_TYPE = "sned_type";

    public static final String AUTH_TYPE = "auth_type";

    public static final String IS_OPEN = "is_open";

    public static final String CREATE_TIME = "create_time";

    public static final String UPDATE_TIME = "update_time";

    @Override
    public Serializable pkVal() {
        return this.id;
    }

}
