package com.github.bogdanlivadariu.reporting.testng.xml.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "exception")
public class ExceptionModel {
    @JacksonXmlProperty(localName = "message")
    public MessageModel message;
    @JacksonXmlProperty(localName = "full-stacktrace")
    public FullStacktraceModel fullStacktrace;
    @JacksonXmlProperty(localName = "class")
    private String clazz;

    public String getClazz() {
        return clazz;
    }

    public MessageModel getMessage() {
        return message;
    }

    public FullStacktraceModel getFullStacktrace() {
        return fullStacktrace;
    }
}
