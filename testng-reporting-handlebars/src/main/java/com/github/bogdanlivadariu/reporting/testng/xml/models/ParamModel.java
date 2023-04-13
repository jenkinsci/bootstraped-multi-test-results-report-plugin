package com.github.bogdanlivadariu.reporting.testng.xml.models;


import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "param")
public class ParamModel {
    @JacksonXmlProperty
    private String index;

    @JacksonXmlProperty
    private String value;
}
