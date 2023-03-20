package com.taki.careerplan.cookbook.controller;

import com.taki.careerplan.cookbook.service.SkuInfoService;
import com.taki.careerplan.domain.dto.SaveOrUpdateSkuDTO;
import com.taki.careerplan.domain.request.SaveOrUpdateSkuRequest;
import com.taki.common.utli.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName GoodsController
 * @Description TODO
 * @Author Long
 * @Date 2023/2/24 19:08
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/goods")
public class GoodsController {


    @Autowired
    private SkuInfoService skuInfoService;
    /*** 
     * @description: 保存 or 修改 商品
     * @param request
     * @return  com.taki.common.utli.ResponseData<com.taki.careerplan.domain.dto.SaveOrUpdateSkuDTO>
     * @author Long
     * @date: 2023/2/24 19:14
     */ 
    @PostMapping("/saveOrUpdate")
    public ResponseData<SaveOrUpdateSkuDTO> saveOrUpdate(SaveOrUpdateSkuRequest request){
        SaveOrUpdateSkuDTO saveOrUpdateSkuDTO =skuInfoService.saveOrUpdate(request);
        return ResponseData.success(saveOrUpdateSkuDTO);
    }


}
