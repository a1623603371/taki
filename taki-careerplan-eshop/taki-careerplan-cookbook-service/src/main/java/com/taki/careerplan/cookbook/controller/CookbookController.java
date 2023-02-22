package com.taki.careerplan.cookbook.controller;

import com.taki.careerplan.cookbook.service.CookbookService;
import com.taki.careerplan.domain.dto.CookbookDTO;
import com.taki.careerplan.domain.dto.SaveOrUpdateCookbookDTO;
import com.taki.careerplan.domain.request.CookbookQueryRequest;
import com.taki.careerplan.domain.request.SaveOrUpdateCookbookRequest;
import com.taki.common.page.PagingInfo;
import com.taki.common.utli.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    /***
     * @description: 查询菜谱信息
     * @param cookbookId 菜谱Id
     * @return
     * @author Long
     * @date: 2023/2/20 17:35
     */
    @GetMapping("/getCookbookInfo/{cookbookId}")
    public ResponseData<CookbookDTO> getCookbookInfo(@PathVariable("cookbookId") Long cookbookId){
        log.info("查询菜谱：cookbookId:{}",cookbookId);


        CookbookQueryRequest request = CookbookQueryRequest.builder().cookbookId(cookbookId).success(true).build();

        return ResponseData.success(cookbookService.getCookbookInfo(request));

    }
    
    /*** 
     * @description:  分页查询 菜谱 集合
     * @param request 查询菜谱请求数据
     * @return
     * @author Long
     * @date: 2023/2/22 20:47
     */ 
    @GetMapping("/listCookbookInfo")
    public  ResponseData<PagingInfo<CookbookDTO>> listCookbookInfo(CookbookQueryRequest  request){
        log.info("查询菜谱：cookbookId:{}",request.getCookbookId());

        return ResponseData.success(cookbookService.listCookbookInfo(request));

    }

}
