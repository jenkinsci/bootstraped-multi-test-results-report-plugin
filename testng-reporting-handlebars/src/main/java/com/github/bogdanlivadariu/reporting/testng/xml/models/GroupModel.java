package com.github.bogdanlivadariu.reporting.testng.xml.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "message")
@XmlAccessorType(XmlAccessType.FIELD)
public class GroupModel {
    @XmlAttribute
    private String name;

    @XmlElement(name = "method")
    private List<MethodModel> methods = new ArrayList<>();

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
