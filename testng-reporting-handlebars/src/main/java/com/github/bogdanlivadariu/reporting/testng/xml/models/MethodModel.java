package com.github.bogdanlivadariu.reporting.testng.xml.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "method")
public class MethodModel {
    private String signature;

    private String name;

    @JacksonXmlProperty(localName = "class")
    private String clazz;

    public String getSignature() {
        return signature;
    }

    public String getName() {
        return name;
    }

    public String getClazz() {
        return clazz;
    }
}
