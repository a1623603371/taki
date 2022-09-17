package com.taki.order.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.taki.common.enums.AfterSaleTypeDetailEnum;
import com.taki.common.page.PagingInfo;
import com.taki.common.utli.ParamCheckUtil;
import com.taki.order.converter.AfterSaleConverter;
import com.taki.order.dao.AfterSaleInfoDao;
import com.taki.order.dao.AfterSaleItemDao;
import com.taki.order.dao.AfterSaleLogDAO;
import com.taki.order.dao.AfterSaleRefundDAO;
import com.taki.order.domain.dto.*;
import com.taki.order.domain.entity.AfterSaleInfoDO;
import com.taki.order.domain.entity.AfterSaleItemDO;
import com.taki.order.domain.entity.AfterSaleLogDO;
import com.taki.order.domain.entity.AfterSaleRefundDO;
import com.taki.order.domain.query.AfterSaleQuery;
import com.taki.order.enums.AfterSaleApplySourceEnum;
import com.taki.order.enums.AfterSaleStatusEnum;
import com.taki.order.enums.BusinessIdentifierEnum;
import com.taki.order.enums.OrderTypeEnum;
import com.taki.order.exception.OrderErrorCodeEnum;
import com.taki.order.service.AfterSaleQueryService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @ClassName AfterSaleQueryServiceImpl
 * @Description 售后查询
 * @Author Long
 * @Date 2022/3/3 22:35
 * @Version 1.0
 */
@Service
public class AfterSaleQueryServiceImpl implements AfterSaleQueryService {


    @Autowired
    private AfterSaleInfoDao afterSaleInfoDao;

    @Autowired
    private AfterSaleItemDao afterSaleItemDao;

    @Autowired
    private AfterSaleLogDAO afterSaleLogDAO;

    @Autowired
    private AfterSaleConverter afterSaleConverter;
    @Autowired
    private AfterSaleRefundDAO afterSaleRefundDAO;
    @Override
    public void checkQueryParam(AfterSaleQuery query) {

        ParamCheckUtil.checkObjectNonNull(query.getBusinessIdentifier(), OrderErrorCodeEnum.BUSINESS_IDENTIFIER_IS_NULL);

        checkIntAllowableValues(query.getBusinessIdentifier(), BusinessIdentifierEnum.allowableValues(),"BusinessIdentifier");

        checkIntSetAllowableValues(query.getOrderTypes(), OrderTypeEnum.allowableValues(),"OrderTypes");

        checkIntSetAllowableValues(query.getAfterSaleTypes(), AfterSaleStatusEnum.allowableValues(),"AfterSaleTypes");

        checkIntSetAllowableValues(query.getApplySources(), AfterSaleApplySourceEnum.allowableValues(),"ApplySources");

        checkIntSetAllowableValues(query.getAfterSaleStatus(), AfterSaleStatusEnum.allowableValues(),"AfterSaleStatus");

        Integer maxSize = AfterSaleQuery.MAX_PAGE_SIZE;

        checkSetMaxSize(query.getAfterSaleIds(),maxSize,"afterSaleIds");

        checkSetMaxSize(query.getOrderIds(),maxSize,"OrderIds");
        checkSetMaxSize(query.getUserIds(),maxSize,"UserIds");
        checkSetMaxSize(query.getSkuCodes(),maxSize,"skuCodes");
    }

    /**
     * @description: 检查 传入的 集合 是否 大于 限定的 值
     * @param afterSaleIds 售后Id 集合
     * @param  maxSize 最大值
     * @param paramName 参数名称
     * @return  void
     * @author Long
     * @date: 2022/4/4 18:20
     */
    private void checkSetMaxSize(Set afterSaleIds, Integer maxSize, String paramName) {
        ParamCheckUtil.checkSetMaxSize(afterSaleIds,maxSize,OrderErrorCodeEnum.COLLECTION_PARAM_CANNOT_BEYOND_MAX_SIZE,paramName,maxSize);

    }

    /**
     * @description: 检查传入的参数是否有枚举对应的
     * @param orderTypes 订单类型集合
     * @param allowableValues 枚举类型集合
     * @param paramName  参数名称
     * @return  void
     * @author Long
     * @date: 2022/4/4 18:14
     */
    private void checkIntSetAllowableValues(Set<Integer> orderTypes, Set<Integer> allowableValues, String paramName) {

        ParamCheckUtil.checkIntSetAllowableValues(orderTypes,allowableValues,
                OrderErrorCodeEnum.ENUM_PARAM_MUST_BE_IN_ALLOWABLE_VALUE,paramName,allowableValues);
    }

    /**
     * @description:  检查传入的参数是否有枚举对应的
     * @param businessIdentifier
     * @param allowableValues 枚举
      * @param paramName 参数名称
     * @return  void
     * @author Long
     * @date: 2022/4/4 18:08
     */
    private void checkIntAllowableValues(Integer businessIdentifier, Set<Integer> allowableValues, String paramName) {
        ParamCheckUtil.checkIntAllowableValues(businessIdentifier,
                allowableValues,OrderErrorCodeEnum.ENUM_PARAM_MUST_BE_IN_ALLOWABLE_VALUE,businessIdentifier,allowableValues);
    }

    @Override
    public PagingInfo<AfterSaleOrderListDTO> executeListQuery(AfterSaleQuery query) {
        // 第一阶段 链表查询
        // 第 2阶段 引入 ES

        if (CollectionUtils.isEmpty(query.getApplySources())){
            //默认只展示用户主动发起的售后单
            query.setApplySources(AfterSaleApplySourceEnum.userApply());
        }
        // 封装 查询数据
        AfterSaleListQueryDTO queryDTO = afterSaleConverter.afterSaleListQueryDTO(query);

        // 查询
        Page<AfterSaleOrderListDTO> page = afterSaleInfoDao.listByPage(queryDTO);

        // 3 转换
        return PagingInfo.toResponse(page.getRecords(), page.getTotal(),(int)page.getCurrent(),(int)page.getSize());
    }

    @Override
    public AfterSaleOrderDetailDTO afterSaleDetail(String afterSaleId) {
        //1 查询 售后单
        AfterSaleInfoDO afterSaleInfo = afterSaleInfoDao.getByAfterSaleId(afterSaleId);

        if (ObjectUtils.isEmpty(afterSaleInfo)){
            return null;
        }

        //2.查询 售后单 条目
        List<AfterSaleItemDO> afterSaleItemDOS = afterSaleItemDao.listByAfterSaleId(afterSaleId);

        // 3.查询售后支付信息
        List<AfterSaleRefundDO> afterSaleRefunds =  afterSaleRefundDAO.listByAfterSaleId(Long.valueOf(afterSaleId));

        // 4、查询日志
        List<AfterSaleLogDO> afterSaleLogs =  afterSaleLogDAO.listByAfterSaleId(Long.valueOf(afterSaleId));

        return   AfterSaleOrderDetailDTO.builder()
                .afterSaleInfo(afterSaleConverter.afterSaleInfoDO2DTO(afterSaleInfo) )
                .afterSaleItems(afterSaleConverter.afterSaleItemDO2DTO(afterSaleItemDOS))
                .afterSalePays(afterSaleConverter.afterSalePayDO2DTO(afterSaleRefunds) )
                .afterSaleLogs(afterSaleConverter.afterSaleLogDO2DTO(afterSaleLogs)).build();
    }

    @Override
    public List<OrderLackItemDTO> getOrderLackItemInfo(String orderId) {
        List<AfterSaleInfoDO> lackItemDO = afterSaleInfoDao.listBy(orderId, Lists.newArrayList(AfterSaleTypeDetailEnum.LACK_REFUND.getCode()));

        if (CollectionUtils.isEmpty(lackItemDO)){
            return null;
        }
        List<OrderLackItemDTO> lackItems = new ArrayList<>();

        lackItemDO.forEach(lackItem -> {
            AfterSaleOrderDetailDTO detailDTO = afterSaleDetail(lackItem.getAfterSaleId());
            OrderLackItemDTO itemDTO = new OrderLackItemDTO();
            BeanUtils.copyProperties(detailDTO,itemDTO);
            lackItems.add(itemDTO);
        });

        return lackItems;
    }
}
