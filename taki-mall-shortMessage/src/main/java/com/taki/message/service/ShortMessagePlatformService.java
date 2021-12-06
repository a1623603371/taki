package com.taki.message.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.taki.core.error.ServiceException;
import com.taki.message.domian.ShortMessagePlatformDO;
import com.taki.message.domian.dto.ShortMessagePlatformDTO;

/**
 * <p>
 *  短信平台服务类
 * </p>
 *
 * @author long
 * @since 2021-12-04
 */
public interface ShortMessagePlatformService {

    /**
     * @description: 保存短信平台信息
     * @param: shortMessagePlatformVO 短信平台信息实体类
     * @return:
     * @date: 2021/12/4 18:02
     */
    void  save(ShortMessagePlatformDTO shortMessagePlatformDTO);
    /**
     * @description: 修改短信平台信息
     * @param: ShortMessagePlatformDTO 短信平台信息实体类
     * @return:
     * @author Long
     * @date: 2021/12/4 18:09
     */
    void update(ShortMessagePlatformDTO shortMessagePlatformDTO);
    /**

    /**
     * @description: 查询开启的短信平台
     * @param:
     * @return: 短信平台信息
     * @author Long
     * @date: 2021/12/4 18:25
     */
    ShortMessagePlatformDTO findTypeByOpen(String type);

    /**
     * @description: 根据Id 查询短信信息平台
     * @param: id 短信信息平台数据ID
     * @return:
     * @author Long
     * @date: 2021/12/4 18:35
     */
    ShortMessagePlatformDTO findById(Long id);

    /***
     * @description: 发送短信
     * @param: phone 手机
     * @param:code 短信验证码
     * @param:type 类型
     * @return: 发送结果
     * @author Long
     * @date: 2021/12/6 17:13
     */
    Boolean sendMessage(String phone, String code, String type) throws ServiceException;
}
