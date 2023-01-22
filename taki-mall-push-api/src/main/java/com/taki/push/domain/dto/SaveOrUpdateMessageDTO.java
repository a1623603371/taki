package com.taki.push.domain.dto;

import lombok.Data;

/**
 * @ClassName SaveOrUpdateMessageDTO
 * @Description 创建/修改活动返回结果
 * @Author Long
 * @Date 2022/10/5 16:09
 * @Version 1.0
 */
@Data
public class SaveOrUpdateMessageDTO {

    /**
     * 新增/修改是否成功
     */
    private Boolean success;
}
