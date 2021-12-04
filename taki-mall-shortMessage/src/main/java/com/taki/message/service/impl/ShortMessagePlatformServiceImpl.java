package com.taki.message.service.impl;


import cn.hutool.core.convert.Convert;
import com.taki.message.dao.ShortMessagePlatformDao;
import com.taki.message.domian.ShortMessagePlatformDO;
import com.taki.message.domian.dto.ShortMessagePlatformDTO;
import com.taki.message.service.ShortMessagePlatformService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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




    private ShortMessagePlatformDao shortMessagePlatformDao;

    @Autowired
    public ShortMessagePlatformServiceImpl( ShortMessagePlatformDao shortMessagePlatformDao) {
        this.shortMessagePlatformDao = shortMessagePlatformDao;
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
    public void open(Long id, Boolean open) {
        ShortMessagePlatformDTO closeShortMessagePlatform = findOpen();
        closeShortMessagePlatform.setOpen(false);
        update(closeShortMessagePlatform);
        ShortMessagePlatformDTO openShortMessagePlatform = findById(id);
        openShortMessagePlatform.setOpen(open);
        update(openShortMessagePlatform);


    }

    @Override
    public ShortMessagePlatformDTO findOpen() {
        ShortMessagePlatformDO shortMessagePlatformDO = shortMessagePlatformDao.findOpen();
        return Convert.convert(ShortMessagePlatformDTO.class,shortMessagePlatformDO);
    }

    @Override
    public ShortMessagePlatformDTO findById(Long id) {

        ShortMessagePlatformDO shortMessagePlatformDO = shortMessagePlatformDao.findById(id);
        return Convert.convert(ShortMessagePlatformDTO.class,shortMessagePlatformDO);
    }
}
