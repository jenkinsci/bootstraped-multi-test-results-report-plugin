package com.github.bogdanlivadariu.reporting.junit.xml.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

//@XmlRootElement(name = "property")
//@XmlAccessorType(XmlAccessType.FIELD)
@JacksonXmlRootElement(localName = "property")
public class PropertyModel {
//    @XmlAttribute
    private String name;

//    @XmlAttribute
    private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

}
