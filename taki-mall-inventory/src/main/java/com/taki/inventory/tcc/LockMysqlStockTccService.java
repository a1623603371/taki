package com.taki.inventory.tcc;

import com.taki.inventory.domain.dto.DeductStockDTO;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * @ClassName LockMysqlStockTccService
 * @Description 锁定mysql 库存 Seate TCC 模式 service
 * @Author Long
 * @Date 2022/5/12 13:07
 * @Version 1.0
 */
@LocalTCC
public interface LockMysqlStockTccService {
    
    /** 
     * @description: 一阶段方法：扣减销售库存 （saleStockQuantity - saleQuantity）
     * @param actionContext
     * @param deductStock
     * @return  boolean
     * @author Long
     * @date: 2022/5/12 13:10
     */
    @TwoPhaseBusinessAction(name = "lockMysqlStockTccService",commitMethod = "commit",rollbackMethod ="rollback" )
    boolean deductStock(BusinessActionContext actionContext,@BusinessActionContextParameter(paramName = "deductStock") DeductStockDTO deductStock);



    /** 
     * @description:  二阶段方法：增加已销售库存（saledStockQuantity+saleQuantity）
     * @param actionContext
     * @return  void
     * @author Long
     * @date: 2022/5/12 13:13
     */ 
    void commit(BusinessActionContext  actionContext);

    /** 
     * @description: 回滚：增加销售库存（saleStockQuantity+SaleQuantity）
     * @param actionContext
     * @return  void
     * @author Long
     * @date: 2022/5/12 13:15
     */ 
    void rollback(BusinessActionContext actionContext);
}
