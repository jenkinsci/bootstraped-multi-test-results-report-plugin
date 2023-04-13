package com.github.bogdanlivadariu.reporting.testng.xml.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JacksonXmlRootElement(localName = "full-stacktrace")
public class FullStacktraceModel {

    @JacksonXmlText
    private String value;

    public String getValue() {
        return value;
    }
}
