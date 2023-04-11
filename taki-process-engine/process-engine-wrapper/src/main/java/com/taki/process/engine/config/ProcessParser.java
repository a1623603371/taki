package com.taki.process.engine.config;

import com.taki.process.engine.model.ProcessorModel;

import java.util.List;

/**
 * @ClassName ProcessParser
 * @Description TODO
 * @Author Long
 * @Date 2023/4/11 21:30
 * @Version 1.0
 */
public interface ProcessParser {

    List<ProcessorModel> parse() throws Exception;
}
