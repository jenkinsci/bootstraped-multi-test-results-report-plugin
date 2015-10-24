package com.github.bogdanlivadariu.reporting.rspec.xml.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

@XmlRootElement(name = "failure")
@XmlAccessorType(XmlAccessType.FIELD)
public class FailureModel {
    @XmlAttribute
    private String message;

    @XmlAttribute
    private String type;

    @XmlValue
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

}
