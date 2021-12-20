package com.github.bogdanlivadariu.reporting.testng.xml.models;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "exception")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExceptionModel {
    @XmlAttribute(name = "class")
    private String clazz;

    @XmlElement(name = "message")
    public MessageModel message;

    @XmlElement(name = "full-stacktrace")
    public FullStacktraceModel fullStacktrace;

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
