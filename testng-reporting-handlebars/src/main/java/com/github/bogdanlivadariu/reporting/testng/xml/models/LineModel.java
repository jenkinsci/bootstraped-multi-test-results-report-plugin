package com.github.bogdanlivadariu.reporting.testng.xml.models;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "line")
@XmlAccessorType(XmlAccessType.FIELD)
public class LineModel {
    @XmlValue
    private String value;

    public String getValue() {
        return value.replaceAll("\n", "").trim();
    }
}
