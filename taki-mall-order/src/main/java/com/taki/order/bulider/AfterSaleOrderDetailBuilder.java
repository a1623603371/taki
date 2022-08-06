package com.taki.order.bulider;

import com.taki.common.utli.ObjectUtil;
import com.taki.order.domain.dto.*;
import com.taki.order.domain.entity.AfterSaleInfoDO;
import com.taki.order.domain.entity.AfterSaleItemDO;
import com.taki.order.domain.entity.AfterSaleLogDO;
import com.taki.order.domain.entity.AfterSaleRefundDO;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * @ClassName AfterSaleOrderDetailBuilder
 * @Description 售后详情构造器
 * @Author Long
 * @Date 2022/4/4 23:33
 * @Version 1.0
 */
public class AfterSaleOrderDetailBuilder {

        private AfterSaleOrderDetailDTO afterSaleOrderDetailDTO = new AfterSaleOrderDetailDTO();


        public AfterSaleOrderDetailBuilder afterSaleInfo(AfterSaleInfoDO afterSaleInfo){

            if (ObjectUtils.isEmpty(afterSaleInfo)){
                afterSaleOrderDetailDTO.setAfterSaleInfo(afterSaleInfo.clone(AfterSaleInfoDTO.class));
            }

            return this;
        }

        public AfterSaleOrderDetailBuilder afterSaleItems(List<AfterSaleItemDO> afterSaleItems){
            if (CollectionUtils.isEmpty(afterSaleItems)){
                afterSaleOrderDetailDTO.setAfterSaleItems(ObjectUtil.convertList(afterSaleItems,AfterSaleItemDTO.class));
            }

            return this;

        }

        public AfterSaleOrderDetailBuilder afterSalePays(List<AfterSaleRefundDO> afterSalePays){
            if (CollectionUtils.isEmpty(afterSalePays)){
                afterSaleOrderDetailDTO.setAfterSalePays(ObjectUtil.convertList(afterSalePays,AfterSalePayDTO.class));
            }
            return this;
        }


    public AfterSaleOrderDetailBuilder afterSalePLogs(List<AfterSaleLogDO> afterSaleLogs){
        if (CollectionUtils.isEmpty(afterSaleLogs)){
            afterSaleOrderDetailDTO.setAfterSaleLogs(ObjectUtil.convertList(afterSaleLogs,AfterSaleLogDTO.class));
        }
        return this;
    }

    public AfterSaleOrderDetailDTO build(){
            return afterSaleOrderDetailDTO;
    }

}
