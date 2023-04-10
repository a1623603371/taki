package com.taki.elasitcserach.controller;

import com.taki.common.utli.ResponseData;
import com.taki.elasitcserach.domain.request.AutoCompleteRequest;
import com.taki.elasitcserach.domain.request.SpellingCorrectionRequest;
import com.taki.elasitcserach.service.CommonSearchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName CommonController
 * @Description TODO
 * @Author Long
 * @Date 2023/3/20 21:57
 * @Version 1.0
 */
@RestController
@RequestMapping("/api/common")
@Slf4j
public class CommonController {


    @Autowired
    private CommonSearchService commonSearchService;

    /*** 
     * @description:  通用服务 补全接口
     * @param request
     * @return  com.taki.common.utli.ResponseData<java.util.List<java.lang.String>>
     * @author Long
     * @date: 2023/3/20 22:38
     */ 
    @PostMapping("/autoComplete")
    public ResponseData<List<String>> autoComplete(@RequestBody AutoCompleteRequest request){
        List<String> completedWords = null;
        try {
            completedWords = commonSearchService.autoComplete(request);
        } catch (IOException e) {
            log.error("补全出现错误：{}",e);
            e.printStackTrace();
        }
        return ResponseData.success(completedWords);
    }


    /*** 
     * @description:  拼音自动补全 纠错
     * @param request
     * @return  com.taki.common.utli.ResponseData<java.lang.String>
     * @author Long
     * @date: 2023/3/21 19:32
     */ 
    @PostMapping("/spellingCorrection")
    public ResponseData<String> spellingCorrection(@RequestBody SpellingCorrectionRequest request){

        String correctedWord = null;
        try {
            correctedWord = commonSearchService.spellingCorrection(request);
        } catch (IOException e) {
            log.error("拼音补全出现错误：{}",e);
            e.printStackTrace();
        }

        return  ResponseData.success(correctedWord);
    }
}
