package com.taki.elasitcserach.service;

import com.taki.elasitcserach.domain.request.AutoCompleteRequest;
import com.taki.elasitcserach.domain.request.SpellingCorrectionRequest;

import java.io.IOException;
import java.util.List;

/**
 * @ClassName CommonSearchService
 * @Description TODO
 * @Author Long
 * @Date 2023/3/20 21:58
 * @Version 1.0
 */
public interface CommonSearchService {
    /*** 
     * @description: 输入内容自动补全接口
     * @param request
     * @return  java.util.List<java.lang.String>
     * @author Long
     * @date: 2023/3/20 22:48
     */ 

    List<String> autoComplete(AutoCompleteRequest request) throws IOException;
    
    /*** 
     * @description: 拼音自动补全 纠错
     * @param request
     * @return  java.lang.String
     * @author Long
     * @date: 2023/3/20 23:02
     */ 
    String spellingCorrection(SpellingCorrectionRequest request) throws IOException;
}
