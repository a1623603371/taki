package com.taki.order.schedule;

import com.taki.common.enums.OrderStatusEnum;
import com.taki.order.config.OrderProperties;
import com.taki.order.dao.OrderInfoDao;
import com.taki.order.domain.entity.OrderInfoDO;
import com.taki.order.domain.request.CancelOrderRequest;
import com.taki.order.enums.OrderCancelTypeEnum;
import com.taki.order.service.OrderAfterSaleService;
import com.taki.order.service.OrderInfoService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.Optional;

/**
 * @ClassName AutoCancelExpiredOrderTask
 * @Description 自动取消超时订单任务
 * @Author Long
 * @Date 2022/5/20 16:42
 * @Version 1.0
 */
@Slf4j
@Component
public class AutoCancelExpiredOrderTask {




    @Autowired
    private OrderInfoDao orderInfoDao;

    @Autowired
    private OrderAfterSaleService orderAfterSaleService;

    @Autowired
    private OrderProperties orderProperties;


    /**
     * @description: 执行业务逻辑
     * @param
     * @return  void
     * @author Long
     * @date: 2022/5/20 16:44
     */
    @XxlJob("autoCancelExpiredOrderTask")
    public void execute(){

        int shardIndex = Optional.ofNullable(XxlJobHelper.getShardIndex()).orElse(0);

        int totalShardNum = Optional.ofNullable(XxlJobHelper.getShardTotal()).orElse(0);

        // 扫描当前时间 - 订单超时时间 -> 前的一小段时间范围(时间范围用配置中心配置)
        // 比如当前时间11:40，订单超时时间是30分钟，扫描11:09:00 -> 11:10:00这一分钟的未支付订单，
        // 缺点：有一个订单超过了30 + 1 = 31分钟，都没有被处理(取消)，这笔订单就永久待支付

        orderInfoDao.listAllUnPaid().forEach(orderInfoDO -> {



            if (totalShardNum <= 0){
                // 不进行分片
                doExecute(orderInfoDO);
            }else {

                //分片
                int hash = hash(orderInfoDO.getOrderId())  %  totalShardNum;

                if (hash == shardIndex){
                    doExecute(orderInfoDO);
                }

            }

        });

        XxlJobHelper.handleSuccess();
    }

    private void doExecute(OrderInfoDO orderInfoDO) {


        if (System.currentTimeMillis() - orderInfoDO.getExpireTime().toInstant(ZoneOffset.ofHours(8)).toEpochMilli()
                >= orderProperties.getExpireTime()){
            // 超过 30 min 未支付
            CancelOrderRequest cancelOrderRequest  = new CancelOrderRequest();

            cancelOrderRequest.setOrderId(orderInfoDO.getOrderId());
            cancelOrderRequest.setOrderStatus(orderInfoDO.getOrderStatus());
            cancelOrderRequest.setCancelType(OrderCancelTypeEnum.TIMEOUT_CANCELED.getCode());
            cancelOrderRequest.setOrderType(orderInfoDO.getOrderType());
            cancelOrderRequest.setUserId(orderInfoDO.getUserId());
            cancelOrderRequest.setBusinessIdentifier(orderInfoDO.getBusinessIdentifier());

            try {
                orderAfterSaleService.cancelOrder(cancelOrderRequest);
            }catch (Exception e){
                log.error("AutoCancelExpiredOrderTask execute error:",e);
            }


        }

    }


    /**
     * @description: 计算hash 值
     * @param orderId 订单Id
     * @return  int
     * @author Long
     * @date: 2022/6/14 17:46
     */
    private int hash(String orderId){
        // 解决 取模可能为负数的情况

        return orderId.hashCode() & Integer.MAX_VALUE;

    }
}
