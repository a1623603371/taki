package com.taki.process.engine.model;

import com.taki.process.engine.enums.InvokeMethod;
import lombok.Data;

/**
 * @ClassName ProcessorNodeModel
 * @Description TODO
 * @Author Long
 * @Date 2023/4/11 20:33
 * @Version 1.0
 */
@Data
public class ProcessorNodeModel {

    private String name;

    private String className;

    private String nextNode;

    private boolean begin = false;

    private InvokeMethod invokeMethod;
}
