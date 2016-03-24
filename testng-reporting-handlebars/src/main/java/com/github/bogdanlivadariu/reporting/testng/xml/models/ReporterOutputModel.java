package com.github.bogdanlivadariu.reporting.testng.xml.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "reporter-output")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReporterOutputModel {
    @XmlElement(name = "line")
    private List<LineModel> lines = new ArrayList<>();

    public List<LineModel> getLines() {
        return lines;
    }
}
