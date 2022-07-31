package com.taki.address.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName ProvinceDO
 * @Description 省设置
 * @Author Long
 * @Date 2022/7/31 16:25
 * @Version 1.0
 */
@Data
@TableName("province")
public class ProvinceDO extends AbstractObject {


    /**
     * 省份代码
     */
    private String code;

    /**
     * 省份名称
     */
    private String name;

    /**
     * 简称
     */
    private String shortName;

    /**
     * 经度
     */
    private String lng;

    /**
     * 纬度
     */
    private String lat;

    /**
     * 排序
     */
    private Integer sort;


}
