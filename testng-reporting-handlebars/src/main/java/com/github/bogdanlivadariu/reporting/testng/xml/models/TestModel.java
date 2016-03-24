package com.github.bogdanlivadariu.reporting.testng.xml.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "test")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestModel {

    @XmlAttribute
    private String name;

    @XmlAttribute(name = "duration-ms")
    private String durationMs;

    @XmlAttribute(name = "started-at")
    private String startedAt;

    @XmlAttribute(name = "finished-at")
    private String finishedAt;

    @XmlElement(name = "class")
    private List<ClassModel> classes = new ArrayList<>();

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
