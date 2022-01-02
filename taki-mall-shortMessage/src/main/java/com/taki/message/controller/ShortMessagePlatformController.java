package com.taki.message.controller;


import com.taki.common.utlis.ResponseData;
import com.taki.message.domian.entity.ShortMessagePlatformDO;
import com.taki.message.domian.dto.ShortMessagePlatformDTO;
import com.taki.message.domian.vo.ShortMessagePlatformVO;
import com.taki.message.service.ShortMessagePlatformService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@Api(tags = "短信相关")
public class ShortMessagePlatformController {

    /**
     * 短信前端service 组件
     */
    private ShortMessagePlatformService shortMessagePlatformService;

    @Autowired
    public ShortMessagePlatformController(ShortMessagePlatformService shortMessagePlatformService) {
        this.shortMessagePlatformService = shortMessagePlatformService;
    }

    @ApiModelProperty("全部短信平台信息")
    @PostMapping("/list")
    public ResponseData<List<ShortMessagePlatformDO>> list(){
        return ResponseData.success(shortMessagePlatformService.getList());
    }

    /**
     * @description: 保存短信平台信息
     * @param: shortMessagePlatformVO 短信平台信息实体类
     * @return:
     * @date: 2021/12/4 18:02
     */
    @ApiModelProperty("保存短信平台信息")
    @PostMapping("/save")
    public ResponseData save(@RequestBody ShortMessagePlatformVO shortMessagePlatformVO){

           ShortMessagePlatformDTO shortMessagePlatformDTO = shortMessagePlatformVO.clone(ShortMessagePlatformDTO.class);
           shortMessagePlatformService.save(shortMessagePlatformDTO);

        return ResponseData.success();
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
        ShortMessagePlatformDTO shortMessagePlatformDTO = shortMessagePlatformVO.clone(ShortMessagePlatformDTO.class);
        Boolean result =  shortMessagePlatformService.update(shortMessagePlatformDTO);
        return ResponseData.success();
    }


    /**
     * @description: 发送短信接口
     * * @param: areaCode 区号
     * @param: phone 手机号
     * @param:  code 短信
     * @param:  type 类型
     * @return:
     * @author Long
     * @date: 2021/12/6 10:55
     */
    @ApiModelProperty("发送短信接口")
    @PostMapping("/sendMessage")
    public ResponseData<Boolean> sendMessage(@ApiParam("区号") @RequestParam("areaCode")String areaCode,@ApiParam("手机号") @RequestParam("phone") String phone, @ApiParam("短信类型")@RequestParam("type") String type) throws Exception {

       Boolean result =   shortMessagePlatformService.sendMessage(areaCode,phone,type);

        return ResponseData.success(result);
    }
}
