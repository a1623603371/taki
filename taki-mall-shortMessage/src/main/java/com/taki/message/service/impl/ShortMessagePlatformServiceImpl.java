package com.taki.message.service.impl;


import cn.hutool.core.convert.Convert;
import com.taki.core.enums.CodeEnum;
import com.taki.core.error.ServiceException;
import com.taki.message.dao.ShortMessagePlatformDao;
import com.taki.message.domian.ShortMessagePlatformDO;
import com.taki.message.domian.dto.ShortMessagePlatformDTO;
import com.taki.message.service.SendMessageStrategy;
import com.taki.message.service.ShortMessagePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * <p>
 *  短信平台服务实现类
 * </p>
 *
 * @author long
 * @since 2021-12-04
 */
@Service
public class ShortMessagePlatformServiceImpl implements ShortMessagePlatformService {




    private final ShortMessagePlatformDao shortMessagePlatformDao;


    private final  ShortMessageManage shortMessageManage;

    @Autowired
    public ShortMessagePlatformServiceImpl( ShortMessagePlatformDao shortMessagePlatformDao,ShortMessageManage shortMessageManage) {
        this.shortMessagePlatformDao = shortMessagePlatformDao;
        this.shortMessageManage = shortMessageManage;
    }

    @Override
    public void save(ShortMessagePlatformDTO shortMessagePlatformDTO) {
        ShortMessagePlatformDO shortMessagePlatformDO = Convert.convert(ShortMessagePlatformDO.class,shortMessagePlatformDTO);
        shortMessagePlatformDao.save(shortMessagePlatformDO);
    }

    @Override
    public void update(ShortMessagePlatformDTO shortMessagePlatformDTO) {
        ShortMessagePlatformDO shortMessagePlatformDO = Convert.convert(ShortMessagePlatformDO.class,shortMessagePlatformDTO);
        shortMessagePlatformDao.update(shortMessagePlatformDO);
    }



    @Override
    public ShortMessagePlatformDTO findTypeByOpen(String type) {
        ShortMessagePlatformDO shortMessagePlatformDO = shortMessagePlatformDao.findTypeByOpen(type);
        return Convert.convert(ShortMessagePlatformDTO.class,shortMessagePlatformDO);
    }

    @Override
    public ShortMessagePlatformDTO findById(Long id) {

        ShortMessagePlatformDO shortMessagePlatformDO = shortMessagePlatformDao.findById(id);
        return Convert.convert(ShortMessagePlatformDTO.class,shortMessagePlatformDO);
    }

    @Override
    public Boolean sendMessage(String phone, String code, String type) throws ServiceException {

        ShortMessagePlatformDTO shortMessagePlatform = findTypeByOpen(type);

        if (ObjectUtils.isEmpty(shortMessagePlatform)){
            throw new ServiceException(CodeEnum.BUSINESS_ERROR,"未查到短信平台信息",null);
        }

        SendMessageStrategy sendMessageStrategy = shortMessageManage.getSendMessageStrategy(shortMessagePlatform.getPlatformCode(),shortMessagePlatform.getSendType());


        return sendMessageStrategy.sendMessage(phone,code,shortMessagePlatform);
    }
}
