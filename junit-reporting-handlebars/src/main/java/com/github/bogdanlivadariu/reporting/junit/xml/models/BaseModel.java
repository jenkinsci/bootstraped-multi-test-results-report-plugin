package com.github.bogdanlivadariu.reporting.junit.xml.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlCData;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

public class BaseModel {

    @JacksonXmlProperty
    private String message;

    @JacksonXmlProperty
    private String type;

    @JacksonXmlText
    private String value;

    public String getMessage() {
        return message;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    @JacksonXmlRootElement(localName = "error")
    public static class ErrorModel extends BaseModel {
    }

    @JacksonXmlRootElement(localName = "failure")
    public static class FailureModel extends BaseModel {
    }
}
