package com.taki.push.dao;

import com.taki.common.BaseDAO;
import com.taki.push.domain.entity.PushMessageDO;
import com.taki.push.mapper.PushMessageMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @ClassName PushMessageDAO
 * @Description TODO
 * @Author Long
 * @Date 2022/10/6 21:36
 * @Version 1.0
 */
@Repository
public class PushMessageDAO extends BaseDAO<PushMessageMapper,PushMessageDO> {





    /*** 
     * @description: 查询消息记录
     * @param pushMessageDO
     * @return  java.util.List<com.taki.push.domain.entity.PushMessageDO>
     * @author Long
     * @date: 2022/10/9 23:19
     */ 
    public List<PushMessageDO> queryMessageByCondition(PushMessageDO pushMessageDO) {

        return  this.lambdaQuery().setEntity(pushMessageDO).list();
    }
}
