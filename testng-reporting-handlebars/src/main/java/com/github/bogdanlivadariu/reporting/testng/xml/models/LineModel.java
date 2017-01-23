package com.github.bogdanlivadariu.reporting.testng.xml.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "line")
@XmlAccessorType(XmlAccessType.FIELD)
public class LineModel {
    @XmlValue
    private String value;

    public String getValue() {
        return value.replaceAll("\n", "").trim();
    }
}
