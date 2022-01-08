package com.taki.address.domian.request;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName AddressQuery
 * @Description 地址查询
 * @Author Long
 * @Date 2022/1/8 14:10
 * @Version 1.0
 */
@Data
public class AddressQuery implements Serializable {


    private static final long serialVersionUID = 3100229330161304549L;

    /**
     *省
     */

    private String provinceCode;

    private String province;

    /**
     * 市
     */
    private String cityCode;

    private String city;

    /**
     * 区
     */
    private String areaCode;

    private String area;

    /**
     * 街道
     */
    private String streetCode;

    private String street;
}
