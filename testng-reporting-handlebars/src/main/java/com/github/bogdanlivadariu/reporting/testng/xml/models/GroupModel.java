package com.github.bogdanlivadariu.reporting.testng.xml.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "message")
public class GroupModel {
    private String name;

    @JacksonXmlElementWrapper(localName = "method", useWrapping = false)
    private final List<MethodModel> methods = new ArrayList<>();

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
