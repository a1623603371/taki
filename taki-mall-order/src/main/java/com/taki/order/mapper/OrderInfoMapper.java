package com.taki.order.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.taki.order.domain.dto.OrderListDTO;
import com.taki.order.domain.dto.OrderListQueryDTO;
import com.taki.order.domain.entity.OrderInfoDO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author long
 * @since 2022-01-02
 */
@Mapper
public interface OrderInfoMapper extends BaseMapper<OrderInfoDO> {

    /** 
     * @description: 订单分页 查询
     * @param query 查询条件
     * @return
     * @author Long
     * @date: 2022/3/3 17:29
     */ 
    Page<OrderListDTO> listByPage(Page<OrderListDTO> page,@Param("query") OrderListQueryDTO query);
}
