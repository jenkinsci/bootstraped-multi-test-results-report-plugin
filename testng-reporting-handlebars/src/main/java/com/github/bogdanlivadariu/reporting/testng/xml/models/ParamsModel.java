package com.github.bogdanlivadariu.reporting.testng.xml.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "params")
public class ParamsModel {
    @JacksonXmlElementWrapper(localName = "param", useWrapping = false)
    @JacksonXmlProperty(localName = "param")
    private List<ParamModel> params;

    public List<ParamModel> getParams() {
        return params;
    }
}
