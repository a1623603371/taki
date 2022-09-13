package com.taki.wms.converter;


import com.baomidou.mybatisplus.annotation.Version;
import com.taki.wms.domain.entity.DeliveryOrderDO;
import com.taki.wms.domain.entity.DeliveryOrderItemDO;
import com.taki.wms.domain.request.PickGoodsRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * @ClassName WmsConverter
 * @Description wsm bean 转换类
 *
 * 将bean交给spring管理， --> componentModel = "spring"
 * 目标属性未匹配时系统忽略，不打印警告日志  -->unmappedTargetPolicy = ReportingPolicy.IGNORE
 * 公共的转换组件引用  --> uses = {ConvertUtil.class}
 * @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ConvertUtil.class}, componentModel = "spring")
 *示例小样1
 @Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = ConvertUtil.class, componentModel = "spring")
 *
  * // 指定默认值
 *@Mapping(target = "name", source = "name", defaultValue = "我是默认值")
 *
 * // 使用java可执行代码或函数
 *@Mapping(target = "name", expression = "java(\"OT\" + paramBean.getId())")
 *
 * // 使用常量代替默认值
 *@Mapping(target = "stringConstant", constant = "Constant Value")
 *
 * //日期格式化
 *@Mapping(source = "birthday", target = "birthday", dateFormat = "yyyy-MM-dd HH:mm:ss")
 * // Integer 到 String的转换
 *@Mapping(source = "price", numberFormat = "$#.00")
 * //Date 转字符串
 *@Mapping(source = "currentDate", dateFormat = "dd.MM.yyyy")
 * @Author Long
 * @Date 2022/9/13 21:17
 * @Version 1.0
 */
@Mapper(componentModel = "spring")
public interface WmsConverter {




    /***
     * @description: 拣货商品请求 转换出货单
     * @param pickGoodsRequest 拣货商品请求
     * @return  转换出货单
     * @author Long
     * @date: 2022/9/13 21:33
     */
    DeliveryOrderDO convertDeliveryOrder(PickGoodsRequest pickGoodsRequest);


    /***
     * @description: 订单条目请求转换出货单条目
     * @param orderItemRequest   订单条目请求
     * @return  出货单条目
     * @author Long
     * @date: 2022/9/13 21:32
     */
    DeliveryOrderItemDO convertDeliveryOrderItem(PickGoodsRequest.OrderItemRequest orderItemRequest);

    /*** 
     * @description: 对象转换类
     * @param orderItemRequests 订单条目请求
     * @return
     * @author Long
     * @date: 2022/9/13 21:31
     */ 
    List<DeliveryOrderItemDO> converterDeliveryOrderItemDO(List<PickGoodsRequest.OrderItemRequest> orderItemRequests);
}
