package com.github.bogdanlivadariu.reporting.rspec.xml.models;

import jakarta.xml.bind.annotation.*;

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
