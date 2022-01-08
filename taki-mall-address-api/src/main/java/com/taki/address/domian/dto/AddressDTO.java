package com.taki.address.domian.dto;

import com.taki.common.core.AbstractObject;
import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName AddressDTO
 * @Description 地址
 * @Author Long
 * @Date 2022/1/8 14:13
 * @Version 1.0
 */
@Data
public class AddressDTO  extends AbstractObject implements Serializable {


    private static final long serialVersionUID = -8826920825128512540L;
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

    public AddressDTO(String provinceCode, String province) {
        this.provinceCode = provinceCode;
        this.province = province;
    }

    public AddressDTO(String provinceCode, String province, String cityCode, String city) {
        this.provinceCode = provinceCode;
        this.province = province;
        this.cityCode = cityCode;
        this.city = city;
    }

    public AddressDTO(String provinceCode, String province, String cityCode, String city, String areaCode, String area) {
        this.provinceCode = provinceCode;
        this.province = province;
        this.cityCode = cityCode;
        this.city = city;
        this.areaCode = areaCode;
        this.area = area;
    }

    public AddressDTO(String provinceCode, String province, String cityCode, String city, String areaCode, String area, String streetCode, String street) {
        this.provinceCode = provinceCode;
        this.province = province;
        this.cityCode = cityCode;
        this.city = city;
        this.areaCode = areaCode;
        this.area = area;
        this.streetCode = streetCode;
        this.street = street;
    }
}
