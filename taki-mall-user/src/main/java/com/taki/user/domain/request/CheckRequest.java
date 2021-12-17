package com.taki.user.domain.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @ClassName CheckRequest
 * @Description 检测参数
 * @Author Long
 * @Date 2021/12/17 16:08
 * @Version 1.0
 */
@Data
public class CheckRequest {

    @ApiModelProperty("检测类型")
    @NotBlank(message = "检测类型不能为空")
    private String type;

    @ApiModelProperty("检测值")
    @NotBlank(message = "检测值不能为空")
    private String value;
}
