package com.taki.order.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taki.order.domain.dto.AfterSaleListQueryDTO;
import com.taki.order.domain.dto.AfterSaleOrderListDTO;
import com.taki.order.domain.entity.AfterSaleInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName AfterSaleInfoMapper
 * @Description 售后
 * @Author Long
 * @Date 2022/3/9 16:04
 * @Version 1.0
 */
@Mapper
public interface AfterSaleInfoMapper extends BaseMapper<AfterSaleInfoDO> {

    /** 
     * @description: 售后单页查询
     * @param 
     * @return
     * @author Long
     * @date: 2022/3/9 16:07
     */ 
    Page<AfterSaleOrderListDTO> listByPage(Page<AfterSaleOrderListDTO> page,@Param("query") AfterSaleListQueryDTO query);

}
