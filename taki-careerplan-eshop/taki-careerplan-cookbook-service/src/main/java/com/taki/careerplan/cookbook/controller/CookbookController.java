package com.taki.careerplan.cookbook.controller;

import com.taki.careerplan.cookbook.service.CookbookService;
import com.taki.careerplan.domain.dto.SaveOrUpdateCookbookDTO;
import com.taki.careerplan.domain.request.SaveOrUpdateCookbookRequest;
import com.taki.common.utli.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName CookbookController
 * @Description TODO
 * @Author Long
 * @Date 2023/2/18 22:23
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/cookbook")
@Slf4j
public class CookbookController {

    @Autowired
    private CookbookService cookbookService;

    /*** 
     * @description:  保存  or 修改 菜谱
     * @param request
     * @return  com.taki.common.utli.ResponseData<SaveOrUpdateCookbookDTO>
     * @author Long
     * @date: 2023/2/18 22:50
     */ 
    @PostMapping("/saveOrUpdate")
    public ResponseData<SaveOrUpdateCookbookDTO> saveOrUpdateCookbook(SaveOrUpdateCookbookRequest request){
        log.info("新增菜谱：cookbookId:{}",request);

        return ResponseData.success(cookbookService.saveOrUpdateCookbook(request));
    }


}
