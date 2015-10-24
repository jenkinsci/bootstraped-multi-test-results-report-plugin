package com.github.bogdanlivadariu.reporting.testng.xml.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "full-stacktrace")
@XmlAccessorType(XmlAccessType.FIELD)
public class FullStacktraceModel {

    @XmlValue
    private String value;

    public String getValue() {
        return value;
    }
}
