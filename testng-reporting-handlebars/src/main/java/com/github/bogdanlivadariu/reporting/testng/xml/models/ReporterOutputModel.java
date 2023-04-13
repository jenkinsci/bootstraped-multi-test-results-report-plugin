package com.github.bogdanlivadariu.reporting.testng.xml.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "reporter-output")
public class ReporterOutputModel {
    @JacksonXmlElementWrapper(localName = "line", useWrapping = false)
    @JacksonXmlProperty(localName = "line")
    private final List<LineModel> lines = new ArrayList<>();

    public List<LineModel> getLines() {
        return lines;
    }
}
