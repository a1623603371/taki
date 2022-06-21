package com.taki.inventory.tcc;

import com.taki.inventory.domain.dto.DeductStockDTO;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

/**
 * @ClassName LockRedisStockTccService
 * @Description 锁定redis 锁定 Seate TCC模式 service
 * @Author Long
 * @Date 2022/5/12 13:16
 * @Version 1.0
 */
@LocalTCC
public interface LockRedisStockTccService {


    /** 
     * @description: 一阶段提交
     * @param actionContext
     * @param deductStockDTO 扣减库存数据
     * @return  boolean
     * @author Long
     * @date: 2022/5/12 13:18
     */
    @TwoPhaseBusinessAction(name = "lockRedisStockTccService",commitMethod = "commit",rollbackMethod = "rollback")
    boolean deductStock(BusinessActionContext actionContext,@BusinessActionContextParameter(paramName = "deductStock") DeductStockDTO deductStockDTO);

    /**
     * @description: 二阶段方法：增加已销售库存（saledStockQuantity + saleQuantity）
     * @param
     * @return  void
     * @author Long
     * @date: 2022/5/12 13:19
     */
    void commit(BusinessActionContext actionContext);


    /*** 
     * @description: 回滚：增加销售库存（saleStockQuantity + saleQuantity）
     * @param actionContext
     * @return  void
     * @author Long
     * @date: 2022/5/12 13:21
     */ 
    void rollback(BusinessActionContext actionContext);
}
