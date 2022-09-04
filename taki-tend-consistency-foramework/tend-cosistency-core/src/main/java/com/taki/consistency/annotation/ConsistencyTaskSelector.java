package com.taki.consistency.annotation;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @ClassName ConsistencyTaskSelector
 * @Description 导入框架相关配置
 * @Author Long
 * @Date 2022/9/4 19:44
 * @Version 1.0
 */
public class ConsistencyTaskSelector implements ImportSelector {


    @Override
    public String[] selectImports(AnnotationMetadata annotationMetadata) {
        return new String[]{ComponentScanConfig.class.getName()};
    }
}
