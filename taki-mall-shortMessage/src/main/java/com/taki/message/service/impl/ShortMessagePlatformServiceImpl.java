package com.taki.message.service.impl;


import cn.hutool.core.convert.Convert;
import com.taki.common.constants.RedisCacheKey;
import com.taki.common.exception.ErrorCodeEnum;
import com.taki.common.exception.ServiceException;
import com.taki.common.redis.RedisCache;
import com.taki.common.utlis.ResponseData;
import com.taki.message.dao.ShortMessagePlatformDao;
import com.taki.message.domian.ShortMessagePlatformDO;
import com.taki.message.domian.dto.ShortMessagePlatformDTO;
import com.taki.message.service.SendMessageStrategy;
import com.taki.message.service.ShortMessagePlatformService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * <p>
 *  短信平台服务实现类
 * </p>
 *
 * @author long
 * @since 2021-12-04
 */
@Service
@Slf4j
public class ShortMessagePlatformServiceImpl implements ShortMessagePlatformService {




    private final ShortMessagePlatformDao shortMessagePlatformDao;

    private final  ShortMessageManage shortMessageManage;

    private final RedisCache redisCache;
    @Autowired
    public ShortMessagePlatformServiceImpl( ShortMessagePlatformDao shortMessagePlatformDao,ShortMessageManage shortMessageManage,RedisCache redisCache) {
        this.shortMessagePlatformDao = shortMessagePlatformDao;
        this.shortMessageManage = shortMessageManage;
        this.redisCache = redisCache;
    }

    @Override
    public Boolean save(ShortMessagePlatformDTO shortMessagePlatformDTO) {
        ShortMessagePlatformDO shortMessagePlatformDO = shortMessagePlatformDTO.clone(ShortMessagePlatformDO.class);
       return  shortMessagePlatformDao.save(shortMessagePlatformDO);
    }

    @Override
    public Boolean update(ShortMessagePlatformDTO shortMessagePlatformDTO) {
        ShortMessagePlatformDO shortMessagePlatformDO = shortMessagePlatformDTO.clone(ShortMessagePlatformDO.class);
       return shortMessagePlatformDao.updateById(shortMessagePlatformDO);
    }



    @Override
    public ShortMessagePlatformDTO findTypeByOpen(String type) {
        ShortMessagePlatformDO shortMessagePlatformDO = shortMessagePlatformDao.findTypeByOpen(type);
        return Convert.convert(ShortMessagePlatformDTO.class,shortMessagePlatformDO);
    }

    @Override
    public ShortMessagePlatformDTO findById(Long id) {

        ShortMessagePlatformDO shortMessagePlatformDO = shortMessagePlatformDao.getById(id);
        return Convert.convert(ShortMessagePlatformDTO.class,shortMessagePlatformDO);
    }

    @Override
    public Boolean sendMessage(String areaCode,String phone, String type)  {

        try {
            ShortMessagePlatformDTO shortMessagePlatform = findTypeByOpen(type);
            if (ObjectUtils.isEmpty(shortMessagePlatform)){
                throw new ServiceException(ErrorCodeEnum.BUSINESS_ERROR,"未查到短信平台信息",null);
            }
            SendMessageStrategy sendMessageStrategy = shortMessageManage.getSendMessageStrategy(shortMessagePlatform.getPlatformCode(),shortMessagePlatform.getSendType());
            String code = generateCode(phone);
            return sendMessageStrategy.sendMessage(areaCode,phone,code,shortMessagePlatform);
        }catch (Exception e){
            log.error("发送短信异常",e.getMessage());

        }
        return false;
    }



    /** 
     * @description: 生成随机验证码
     * @param 
     * @return  java.lang.String
     * @author Long
     * @date: 2021/12/21 16:12
     */ 
    private String generateCode(String phone){
        ThreadLocalRandom random =ThreadLocalRandom.current();

        String code =  String.valueOf(random.nextInt(10001,99999));

        // 保存到redis
        redisCache.set(RedisCacheKey.REGISTER_CODE_KEY + phone,code,300);

        return  code;
    }


    @Override
    public List<ShortMessagePlatformDO> getList() {
        return shortMessagePlatformDao.list();
    }
}
