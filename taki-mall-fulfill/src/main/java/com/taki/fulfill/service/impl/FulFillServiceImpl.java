package com.taki.fulfill.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.taki.common.bean.SpringApplicationContext;
import com.taki.common.constants.RedisLockKeyConstants;
import com.taki.common.redis.RedisLock;
import com.taki.common.utli.RandomUtil;
import com.taki.fulfill.builder.FulfillDataBuilder;
import com.taki.fulfill.dao.OrderFulfillDao;
import com.taki.fulfill.dao.OrderFulfillItemDao;
import com.taki.fulfill.domain.entity.OrderFulfillDO;
import com.taki.fulfill.domain.entity.OrderFulfillItemDO;
import com.taki.fulfill.domain.request.ReceiveFulFillRequest;
import com.taki.fulfill.exection.FulfillBizException;
import com.taki.fulfill.exection.FulfillErrorCodeEnum;
import com.taki.fulfill.service.FulfillService;
import io.seata.saga.engine.StateMachineEngine;
import io.seata.saga.statelang.domain.ExecutionStatus;
import io.seata.saga.statelang.domain.StateMachineInstance;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName FulFillServiceImpl
 * @Description 履约 service 服务
 * @Author Long
 * @Date 2022/5/15 18:11
 * @Version 1.0
 */
@Service
@Slf4j
public class FulFillServiceImpl implements FulfillService {

    @Autowired
    private OrderFulfillDao orderFulfillDao;


    @Autowired
    private OrderFulfillItemDao orderFulfillItemDao;

    @Autowired
    private RedisLock redisLock;
    @Autowired
    private SpringApplicationContext springApplicationContext;


    @Override
    public void createFulfillOrder(ReceiveFulFillRequest request) {

        // 1.生成履约单Id
        String fulfillId = genFulfillId();

        // 2 .生成 履约单 和 履约条目
        FulfillDataBuilder builder = FulfillDataBuilder.builder(request).builderOrderFulfill(fulfillId).buildOrderFulfillItem();

        OrderFulfillDO orderFulfill = builder.getOrderFulfill();

        List<OrderFulfillItemDO> orderFulfillItems = builder.getOrderFulFillItems();

        //3. 报错履约和履约条目
        orderFulfillDao.save(orderFulfill);

        orderFulfillItemDao.saveBatch(orderFulfillItems);

    }

    /**
     * 生成 履约单Id
     * @return
     */
    private String genFulfillId() {

        return RandomUtil.genRandomNumber(10);
    }

    @Override
    public void cancelFulfillOrder(String orderId) {

        // 查询 履约单
        OrderFulfillDO orderFulfillDO = orderFulfillDao.getOne(orderId);

        if (ObjectUtils.isNotEmpty(orderFulfillDO)){

            orderFulfillDao.removeById(orderFulfillDO.getId());

            // 3 查询 履约条目

            List<OrderFulfillItemDO> orderFulfillItems = orderFulfillItemDao.listByFulfillId(orderFulfillDO.getFulfillId());

            // 4.删除 履约条目

            orderFulfillItems.forEach(orderFulfillItemDO -> {
                orderFulfillItemDao.removeById(orderFulfillItemDO.getId());
            });

        }

    }

    @Override
    public Boolean receiveOrderFulFill(ReceiveFulFillRequest request) {
        log.info("接受订单履约成功，request:{}", JSONObject.toJSONString(request));

        String  orderId = request.getOrderId();

        // 加入分布式锁
        String lockKey = RedisLockKeyConstants.ORDER_FULFILL_KEY + orderId;

        boolean lock = redisLock.tryLock(lockKey);

        if (!lock){
            throw new FulfillBizException(FulfillErrorCodeEnum.ORDER_FULFILL_ERROR);

        }

        try {

            // 1.幂等：校验 orderId 是否 已履约
            if (orderFulfilled(request.getOrderId())){
                log.info("改订单已履约！！！，orderId= {}",request.getOrderId());

                return true;
            }

            // 2 saga状态机，触发wms 拣货 和 tms 发货
            StateMachineEngine stateMachineEngine = (StateMachineEngine) springApplicationContext.getBean("stateMachineEngine");

            Map<String,Object> stateParam = new HashMap<>(3);
            stateParam.put("receiveFulfillRequest",request);

            // 配置的saga状态机 json的name
            //位于/resource/statelang/order_fufull.json
            String stateMachineName = "order_fulfill";

            log.info("开始触发saga 流程,stateMachineName={}",stateMachineName);
            StateMachineInstance inst = stateMachineEngine.startWithBusinessKey(stateMachineName,null,null,stateParam);

            if (ExecutionStatus.SU.equals(inst.getStatus())){
                log.info("订单履约流程执行完毕，xid = {}",inst.getId() );
            }else {

                log.error("订单履约流程执行异常，xid={}",inst.getId());


                throw new FulfillBizException(FulfillErrorCodeEnum.ORDER_FULFILL_IS_ERROR);
            }

        return true;

        }finally {
            redisLock.unLock(lockKey);
        }

    }

    private boolean orderFulfilled(String orderId) {

        OrderFulfillDO orderFulfillDO = orderFulfillDao.getOne(orderId);

        return orderFulfillDO != null;


    }
}
