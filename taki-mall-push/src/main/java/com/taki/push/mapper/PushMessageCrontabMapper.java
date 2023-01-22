package com.taki.push.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taki.push.domain.entity.PushMessageCrontabDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 消息发送任务表 Mapper 接口
 * </p>
 *
 * @author long
 * @since 2022-10-06
 */
@Mapper
public interface PushMessageCrontabMapper extends BaseMapper<PushMessageCrontabDO> {

}
