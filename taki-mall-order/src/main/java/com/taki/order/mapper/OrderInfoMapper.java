package com.taki.order.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.order.domin.entity.OrderInfoDO;
import org.apache.ibatis.annotations.Mapper;

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

}
