package com.taki.process.engine.config;

import com.taki.process.engine.enums.InvokeMethod;
import com.taki.process.engine.model.ProcessorModel;
import com.taki.process.engine.model.ProcessorNodeModel;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName XmlProcessParser
 * @Description TODO
 * @Author Long
 * @Date 2023/4/11 21:31
 * @Version 1.0
 */
public abstract class XmlProcessParser implements ProcessParser {

    @Override
    public List<ProcessorModel> parse() throws Exception {

        Document document = getDocument();
        Element rootElement = document.getRootElement();
        List<Element> processorElements = rootElement.elements();
        List<ProcessorModel> processorModels = new ArrayList<>();
        processorElements.forEach(processor->{
            ProcessorModel processorModel = new ProcessorModel();
            processorModel.setName(processor.getName());
            List<Element> elements = processor.elements();
            for (Element element :elements) {
                ProcessorNodeModel processorNodeModel = new ProcessorNodeModel();
                processorNodeModel.setName(element.attributeValue("name"));
                processorNodeModel.setClassName(element.attributeValue("class"));
                String next = element.attributeValue("next");
                if (next != null) {
                    processorNodeModel.setNextNode(next);
                }
                String begin = element.attributeValue("begin");
                processorNodeModel.setBegin(Boolean.parseBoolean(begin));
                String invokeMethodStr  = element.attributeValue("invoke-method");
                InvokeMethod invokeMethod = invokeMethodStr == null ? InvokeMethod.SYNC : InvokeMethod.valueOf(invokeMethodStr.toUpperCase());
                processorNodeModel.setInvokeMethod(invokeMethod);
                processorModel.addNode(processorNodeModel);
            }

        processorModels.add(processorModel);
        });



        return processorModels;
    }

    protected abstract Document getDocument() throws DocumentException;
}
