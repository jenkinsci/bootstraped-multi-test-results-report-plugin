package com.github.bogdanlivadariu.reporting.testng.xml.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlText;

@JacksonXmlRootElement(localName = "line")
public class LineModel {
    @JacksonXmlText
    private String value;

    public String getValue() {
        return value.replaceAll("\n", "").trim();
    }
}
