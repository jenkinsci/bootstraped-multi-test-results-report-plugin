package com.github.bogdanlivadariu.reporting.testng.xml.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "test")
public class TestModel {

    @JacksonXmlElementWrapper(localName = "class", useWrapping = false)
    @JacksonXmlProperty(localName = "class")
    private final List<ClassModel> classes = new ArrayList<>();
    private String name;
    @JacksonXmlProperty(localName = "duration-ms")
    private String durationMs;
    @JacksonXmlProperty(localName = "started-at")
    private String startedAt;
    @JacksonXmlProperty(localName = "finished-at")
    private String finishedAt;
    private int totalPassed = 0;

    private int totalFailed = 0;

    private int totalSkipped = 0;

    public void postProcess() {
        for (ClassModel cm : getClasses()) {
            cm.postProcess();
            totalPassed += cm.getTotalPassed();
            totalFailed += cm.getTotalFailed();
            totalSkipped += cm.getTotalSkipped();
        }
    }

    public String getName() {
        return name;
    }

    public String getDurationMs() {
        return durationMs;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public String getFinishedAt() {
        return finishedAt;
    }

    public List<ClassModel> getClasses() {
        return classes;
    }

    public int getTotalPassed() {
        return totalPassed;
    }

    public int getTotalFailed() {
        return totalFailed;
    }

    public int getTotalSkipped() {
        return totalSkipped;
    }

}
