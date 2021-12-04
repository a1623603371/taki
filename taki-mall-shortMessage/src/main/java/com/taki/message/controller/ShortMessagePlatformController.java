package com.taki.message.controller;


import cn.hutool.core.convert.Convert;
import cn.hutool.core.lang.TypeReference;
import com.taki.core.utlis.ResponseData;
import com.taki.message.domian.dto.ShortMessagePlatformDTO;
import com.taki.message.domian.vo.ShortMessagePlatformVO;
import com.taki.message.service.ShortMessagePlatformService;
import io.swagger.annotations.ApiModelProperty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 *  短信前端控制器
 * </p>
 *
 * @author long
 * @since 2021-12-04
 */
@RestController
@RequestMapping("/short-message-platform")
@Slf4j
public class ShortMessagePlatformController {

    /**
     * 短信前端service 组件
     */
    private ShortMessagePlatformService shortMessagePlatformService;

    @Autowired
    public ShortMessagePlatformController(ShortMessagePlatformService shortMessagePlatformService) {
        this.shortMessagePlatformService = shortMessagePlatformService;
    }

    /**
     * @description: 保存短信平台信息
     * @param: shortMessagePlatformVO 短信平台信息实体类
     * @return:
     * @date: 2021/12/4 18:02
     */
    @ApiModelProperty("保存短信平台信息")
    @PostMapping("/save")
    public ResponseData<String> save(@RequestBody ShortMessagePlatformVO shortMessagePlatformVO){

           ShortMessagePlatformDTO shortMessagePlatformDTO = Convert.convert(new TypeReference<ShortMessagePlatformDTO>() {},shortMessagePlatformVO);
           shortMessagePlatformService.save(shortMessagePlatformDTO);
        return ResponseData.success("ok",null);
    }

    /**
     * @description: 修改短信平台信息
     * @param: shortMessagePlatformVO 短信平台信息实体类
     * @return:
     * @author Long
     * @date: 2021/12/4 18:09
     */
    @ApiModelProperty("修改短信平台信息")
    @PutMapping("/update")
    public ResponseData<Boolean> update(@RequestBody ShortMessagePlatformVO shortMessagePlatformVO){
        ShortMessagePlatformDTO shortMessagePlatformDTO = Convert.convert(new TypeReference<ShortMessagePlatformDTO>() {},shortMessagePlatformVO);
        shortMessagePlatformService.update(shortMessagePlatformDTO);
        return ResponseData.success("ok",true);
    }

    /**
     * @description:  开启短信平台
     * @param: id 短信平台信息Id
     * @param:  open 开关
     * @return:
     * @author Long
     * @date: 2021/12/4 18:20
     */
    @ApiModelProperty("开启短信平台")
    @PutMapping("/open")
    public  ResponseData<Boolean> open(Long id,Boolean open){
        shortMessagePlatformService.open(id,open);
        return ResponseData.success("ok",true);
    }
}
