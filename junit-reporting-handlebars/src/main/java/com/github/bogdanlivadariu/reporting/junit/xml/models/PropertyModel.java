package com.github.bogdanlivadariu.reporting.junit.xml.models;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "property")
@XmlAccessorType(XmlAccessType.FIELD)
public class PropertyModel
{
    @XmlAttribute
    private String name;

    @XmlAttribute
    private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
