package com.github.bogdanlivadariu.reporting.testng.xml.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "reporter-output")
@XmlAccessorType(XmlAccessType.FIELD)
public class ReporterOutputModel {
    @XmlElement(name = "line")
    private List<LineModel> lines = new ArrayList<>();

    public List<LineModel> getLines() {
        return lines;
    }
}
