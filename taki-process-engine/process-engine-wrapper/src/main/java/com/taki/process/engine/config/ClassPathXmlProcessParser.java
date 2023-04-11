package com.taki.process.engine.config;

import com.taki.process.engine.process.AbstractProcessor;
import com.taki.process.engine.process.ProcessContext;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

import java.io.InputStream;

/**
 * @ClassName ClassPathXmlProcessParser
 * @Description TODO
 * @Author Long
 * @Date 2023/4/11 21:43
 * @Version 1.0
 */
public class ClassPathXmlProcessParser extends XmlProcessParser {

    private final  String file;

    public ClassPathXmlProcessParser(String file) {
        this.file = file.startsWith("/") ? file : "/" + file;
    }


    @Override
    protected Document getDocument() throws DocumentException {
        InputStream in = getClass().getResourceAsStream(file);
        SAXReader saxReader = new SAXReader();
        return saxReader.read(in);
    }
}
