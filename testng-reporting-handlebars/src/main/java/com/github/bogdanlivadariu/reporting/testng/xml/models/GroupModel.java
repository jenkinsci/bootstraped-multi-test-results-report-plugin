package com.github.bogdanlivadariu.reporting.testng.xml.models;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupModel {
    @XmlAttribute
    private String name;

    @XmlElement(name = "method")
    private List<MethodModel> methods;

    public GroupModel(String name) {
        this.name = name;
    }

    public GroupModel() {
    }

    public String getName() {
        return name;
    }

    public List<MethodModel> getMethods() {
        return methods;
    }

}
