package com.taki.address.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName StreetDO
 * @Description 街道设置
 * @Author Long
 * @Date 2022/7/31 16:25
 * @Version 1.0
 */
@Data
@TableName("street")
public class StreetDO extends AbstractObject {

    /**
     * 街道代码
     */
    private String code;

    /**
     * 父级区代码
     */
    private String areaCode;

    /**
     * 街道名称
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
