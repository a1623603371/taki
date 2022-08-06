package com.taki.address.service.impl;

import com.google.common.collect.Lists;
import com.taki.address.dao.AreaDAO;
import com.taki.address.dao.CityDAO;
import com.taki.address.dao.ProvinceDAO;
import com.taki.address.dao.StreetDAO;
import com.taki.address.domain.entity.AreaDO;
import com.taki.address.domain.entity.CityDO;
import com.taki.address.domain.entity.ProvinceDO;
import com.taki.address.domain.entity.StreetDO;
import com.taki.address.domian.dto.AddressDTO;
import com.taki.address.domian.request.AddressQuery;
import com.taki.address.exception.AddressErrorCodeError;
import com.taki.address.exception.AddressException;
import com.taki.address.service.AddressService;
import com.taki.common.utli.LoggerFormat;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;


import java.util.*;
import java.util.stream.Collectors;

/**
 * @ClassName AddressServiceImpl
 * @Description 用农收货地址 service 组件
 * @Author Long
 * @Date 2022/7/31 16:48
 * @Version 1.0
 */
@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AreaDAO areaDAO;

    @Autowired
    private CityDAO cityDAO;

    @Autowired
    private ProvinceDAO provinceDAO;

    @Autowired
    private StreetDAO streetDAO;

    @Override
    public AddressDTO queryAddress(AddressQuery addressQuery) {

        log.info(LoggerFormat.build()
                .remark("queryAddress -》 request")
                .data("query",addressQuery)
                .finish());

        //1.效验参数
        checkParam(addressQuery);

        //2 查询街道信息
        List<StreetDO> streets = streetDAO.listStreets(addressQuery.getStreetCode(),addressQuery.getStreet());

        if (streetNotEmpty(addressQuery)){
            streets = streetDAO.listStreets(addressQuery.getStreetCode(),addressQuery.getStreet());

            if (CollectionUtils.isEmpty(streets)){
                return null;
            }
        }

        //3.查询区信息
        List<AreaDO> areas = new ArrayList<>();

        if (areaNotEmpty(addressQuery) || !CollectionUtils.isEmpty(streets)){
            Set<String> areaCodes = Optional.ofNullable(streets)
                    .map(street ->  street.stream()
                    .map(StreetDO::getAreaCode)
                    .collect(Collectors.toSet()))
                    .orElse(Collections.EMPTY_SET);
            if (StringUtils.isNotEmpty(addressQuery.getAreaCode())){
                areaCodes.add(addressQuery.getAreaCode());
            }
            areas = areaDAO.listAreas(areaCodes,addressQuery.getArea());

            if(CollectionUtils.isEmpty(areas)){
                return null;
            }
        }

        //4.查询市信息
        List<CityDO> cities = new ArrayList<>();

        if (cityNotEmpty(addressQuery) || !CollectionUtils.isEmpty(areas)){
            Set<String> cityCodes = Optional.ofNullable(areas)
                    .map(area -> area.stream()
                    .map(AreaDO::getCityCode)
                    .collect(Collectors.toSet()))
                    .orElse(Collections.EMPTY_SET);
            if (StringUtils.isNotEmpty(addressQuery.getProvinceCode())){
                cityCodes.add(addressQuery.getCityCode());
            }
            cities = cityDAO.listCities(cityCodes,addressQuery.getCity());

            if (CollectionUtils.isEmpty(cities)){
                return null;
            }

        }

        // 5.查询省信息
        List<ProvinceDO> provinces = new ArrayList<>();

        if (provinceNotEmpty(addressQuery) || !CollectionUtils.isEmpty(cities)){
            Set<String> provinceCodes = Optional
                    .ofNullable(cities).map(city-> city.stream()
                            .map(CityDO::getProvinceCode).collect(Collectors.toSet()))
                    .orElse(Collections.EMPTY_SET);
            if (StringUtils.isNotBlank(addressQuery.getProvinceCode())){
                provinceCodes.add(addressQuery.getProvinceCode());
            }
            provinces = provinceDAO.listProvinces(provinceCodes,addressQuery.getProvince());

            if (CollectionUtils.isEmpty(provinceCodes)){
                return null;
            }
        }

        //6. 组装候选结果集合
        List<AddressDTO> candidates = assembleResult(provinces,cities,areas,streets);


        // 7. 筛选结果
        for (AddressDTO candidate : candidates) {

            if (isMatch(candidate,addressQuery)){
                log.info(LoggerFormat.build()
                        .remark("queryAddress->response")
                        .data("response",candidate)
                        .finish());

                return candidate;

            }


        }
        log.info(LoggerFormat.build()
                .remark("queryAddress->response")
                .finish());

        return null;
    }

    private boolean isMatch(AddressDTO candidate, AddressQuery query) {
        boolean provinceCodeMatch = doMatch(candidate.getProvinceCode(), query.getProvinceCode());
        boolean provinceMatch = doMatch(candidate.getProvince(), query.getProvince());
        boolean cityCodeMatch = doMatch(candidate.getCityCode(), query.getCityCode());
        boolean cityMatch = doMatch(candidate.getCity(), query.getCity());
        boolean areaCodeMatch = doMatch(candidate.getAreaCode(), candidate.getAreaCode());
        boolean areaMatch = doMatch(candidate.getArea(), query.getArea());
        boolean streetCodeMatch = doMatch(candidate.getStreetCode(), query.getStreetCode());
        boolean streetMatch = doMatch(candidate.getStreet(), query.getStreet());

        return provinceCodeMatch && provinceMatch && cityCodeMatch && cityMatch
                && areaCodeMatch && areaMatch && streetCodeMatch && streetMatch;
    }

    private boolean doMatch(String candidate, String query) {
        if (Objects.isNull(query) || com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(query)) {
            return true;
        }
        if (Objects.isNull(candidate) || com.baomidou.mybatisplus.core.toolkit.StringUtils.isBlank(candidate)) {
            return false;
        }
        return candidate.equals(query);
    }

    /** 
     * @description: 组装返参
     * @param provinces 省
     * @param cities 城市
     * @param areas 地区
     * @param streets 街道
     * @return
     * @author Long
     * @date: 2022/7/31 19:30
     */ 
    private List<AddressDTO> assembleResult(List<ProvinceDO> provinces, List<CityDO> cities, List<AreaDO> areas, List<StreetDO> streets) {

        List<AddressDTO> result = new ArrayList<>();

        provinces.forEach(provinceDO -> {
            result.addAll(assembleResult(provinceDO,cities,areas,streets));
        });

        return result;
    }

    /**
     * @description:
     * @param provinceDO
     * @param cities
     * @param areas
     * @param streets
     * @return  java.util.List<com.taki.address.domian.dto.AddressDTO>
     * @author Long
     * @date: 2022/7/31 21:18
     */
    private List<AddressDTO> assembleResult(ProvinceDO provinceDO,List<CityDO> cities, List<AreaDO> areas, List<StreetDO> streets){
        //过滤出 province 下city

        cities = cities.stream().filter(city->city.getProvinceCode().equals(provinceDO.getCode())).collect(Collectors.toList());

        if (CollectionUtils.isEmpty(cities)){
            return Lists.newArrayList(new AddressDTO(provinceDO.getCode(),provinceDO.getName()));
        }

        List<AddressDTO> result = new ArrayList<>();

        cities.forEach(city->{
            result.addAll(assembleResult(provinceDO,city,areas,streets));
        });

        return result;
    }

    private List<AddressDTO> assembleResult(ProvinceDO province, CityDO city, List<AreaDO> areas
            , List<StreetDO> streets) {

        //过滤出city下的area
        areas = areas.stream().filter(area -> area.getCityCode().equals(city.getCode()))
                .collect(Collectors.toList());

        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isEmpty(areas)) {
            return Lists.newArrayList(new AddressDTO(province.getCode(), province.getName(),
                    city.getCode(), city.getName()));
        }

        List<AddressDTO> result = new ArrayList<>();
        for (AreaDO area : areas) {
            result.addAll(assembleResult(province, city, area, streets));
        }
        return result;
    }

    private List<AddressDTO> assembleResult(ProvinceDO province, CityDO city, AreaDO area
            , List<StreetDO> streets) {

        //过滤出area下的street
        streets = streets.stream().filter(street -> street.getAreaCode().equals(area.getCode()))
                .collect(Collectors.toList());

        if (com.baomidou.mybatisplus.core.toolkit.CollectionUtils.isEmpty(streets)) {
            return Lists.newArrayList(new AddressDTO(province.getCode(), province.getName(),
                    city.getCode(), city.getName(), area.getCode(), area.getName()));
        }

        List<AddressDTO> result = new ArrayList<>();
        for (StreetDO street : streets) {
            result.add(new AddressDTO(province.getCode(), province.getName(),
                    city.getCode(), city.getName(), area.getCode(), area.getName(),
                    street.getCode(), street.getName()));
        }
        return result;
    }

    /**
     * @description:  省不能为空判断
     * @param addressQuery 地址查询条件
     * @return  boolean
     * @author Long
     * @date: 2022/7/31 18:32
     */
    private boolean provinceNotEmpty(AddressQuery addressQuery) {

        return StringUtils.isNotBlank(addressQuery.getProvince()) || StringUtils.isNotBlank(addressQuery.getProvinceCode());
    }

    /**
     * @description: 城市不能为空判断
     * @param addressQuery 地址查询条件
     * @return  boolean
     * @author Long
     * @date: 2022/7/31 18:17
     */
    private boolean cityNotEmpty(AddressQuery addressQuery) {

        return StringUtils.isNotEmpty(addressQuery.getCity()) || StringUtils.isNotEmpty(addressQuery.getCityCode());
    }

    /**
     * @description: 判断 地区不为空
     * @param addressQuery  地址查询条件
     * @return  boolean
     * @author Long
     * @date: 2022/7/31 17:13
     */
    private boolean areaNotEmpty(AddressQuery addressQuery) {

        return StringUtils.isNotEmpty(addressQuery.getArea()) || StringUtils.isNotEmpty(addressQuery.getAreaCode());
    }

    /** 
     * @description: 判断街道 不为空
     * @param addressQuery 地址查询
     * @return  boolean
     * @author Long
     * @date: 2022/7/31 17:06
     */ 
    private boolean streetNotEmpty(AddressQuery addressQuery) {

        return  StringUtils.isNotBlank(addressQuery.getStreet()) || StringUtils.isNotBlank(addressQuery.getStreetCode());
    }

    /***
     * @description: 检查参数
     * @param addressQuery 地址查询条件
     * @return  void
     * @author Long
     * @date: 2022/7/31 16:52
     */
    private void checkParam(AddressQuery addressQuery) {

        if(allEmpty(addressQuery)){
            throw new AddressException(AddressErrorCodeError.PARAM_CANNOT_ALL_EMPTY);
        }


    }

    private boolean allEmpty(AddressQuery addressQuery) {

        return StringUtils.isBlank(addressQuery.getProvince())
                && StringUtils.isBlank(addressQuery.getProvinceCode())
                && StringUtils.isBlank(addressQuery.getArea())
                && StringUtils.isBlank(addressQuery.getAreaCode())
                && StringUtils.isBlank(addressQuery.getCity())
                && StringUtils.isBlank(addressQuery.getCityCode())
                && StringUtils.isBlank(addressQuery.getStreet())
                && StringUtils.isBlank(addressQuery.getStreetCode());





    }
}
