package com.taki.message.service;


import com.baomidou.mybatisplus.extension.service.IService;
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
     * @description:  开启短信平台
     * @param: id 短信平台信息Id
     * @param:  open 开关
     * @return:
     * @author Long
     * @date: 2021/12/4 18:20
     */
    void open(Long id, Boolean open);

    /**
     * @description: 查询开启的短信平台
     * @param:
     * @return: 短信平台信息
     * @author Long
     * @date: 2021/12/4 18:25
     */
    ShortMessagePlatformDTO findOpen();

    /**
     * @description: 根据Id 查询短信信息平台
     * @param: id 短信信息平台数据ID
     * @return:
     * @author Long
     * @date: 2021/12/4 18:35
     */
    ShortMessagePlatformDTO findById(Long id);
}
