package com.taki.address.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName CityDO
 * @Description 城市设置
 * @Author Long
 * @Date 2022/7/31 16:25
 * @Version 1.0
 */
@Data
@TableName("city")
public class CityDO extends AbstractObject {



    /**
     * 市代码
     */
    private String code;

    /**
     * 市名称
     */
    private String name;

    /**
     * 简称
     */
    private String shortCode;

    /**
     * 省代码
     */
    private String provinceCode;

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
