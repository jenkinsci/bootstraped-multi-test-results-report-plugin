package com.github.bogdanlivadariu.reporting.testng.xml.models;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class MessageModel {

    @XmlValue
    private String value;

    public String getValue() {
        return value;
    }
}
