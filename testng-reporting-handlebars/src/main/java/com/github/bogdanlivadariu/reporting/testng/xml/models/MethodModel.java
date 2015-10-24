package com.github.bogdanlivadariu.reporting.testng.xml.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "method")
@XmlAccessorType(XmlAccessType.FIELD)
public class MethodModel {
    @XmlAttribute
    private String signature;

    @XmlAttribute
    private String name;

    @XmlAttribute(name = "class")
    private String clazz;

    public String getSignature() {
        return signature;
    }

    public String getName() {
        return name;
    }

    public String getClazz() {
        return clazz;
    }
}
