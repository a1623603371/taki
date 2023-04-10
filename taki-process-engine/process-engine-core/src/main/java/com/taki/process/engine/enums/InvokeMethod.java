package com.taki.process.engine.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @ClassName InvokeMethod
 * @Description TODO
 * @Author Long
 * @Date 2023/4/10 22:54
 * @Version 1.0
 */
@Getter
@AllArgsConstructor
public enum InvokeMethod {

    SYNC("同步"),
    ASYNC("异步");

    private  String desc;


}
