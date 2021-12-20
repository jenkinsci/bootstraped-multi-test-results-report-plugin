package com.github.bogdanlivadariu.reporting.testng.xml.models;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "full-stacktrace")
@XmlAccessorType(XmlAccessType.FIELD)
public class FullStacktraceModel {

    @XmlValue
    private String value;

    public String getValue() {
        return value;
    }
}
