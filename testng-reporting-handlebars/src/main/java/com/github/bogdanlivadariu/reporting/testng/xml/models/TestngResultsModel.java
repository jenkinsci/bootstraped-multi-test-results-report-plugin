package com.github.bogdanlivadariu.reporting.testng.xml.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.ArrayList;
import java.util.List;

@JacksonXmlRootElement(localName = "testng-results")
public class TestngResultsModel {
    @JacksonXmlElementWrapper(localName = "suite", useWrapping = false)
    @JacksonXmlProperty(localName = "suite")
    private final List<SuiteModel> suites = new ArrayList<>();
    @JacksonXmlProperty
    private String skipped;
    @JacksonXmlProperty
    private String failed;
    @JacksonXmlProperty
    private String total;
    @JacksonXmlProperty
    private String passed;
    @JacksonXmlProperty
    private String ignored;
    @JacksonXmlProperty(localName = "reporter-output")
    private ReporterOutputModel reporterOutput;
    private Long totalTime = (long) 0;
    private int totalClasses;

    private int totalClassesTests = 0;

    private int totalClassesFailed = 0;

    private int totalClassesPassed = 0;

    private int totalClassesSkipped = 0;

    public void postProcess() {
        for (SuiteModel sm : getSuites()) {
            sm.postProcess();
            totalTime += sm.getDurationMs();
            totalClasses += sm.getTotalClasses();
            totalClassesTests += sm.getTotalTests();
            totalClassesFailed += sm.getTotalFailed();
            totalClassesPassed += sm.getTotalPassed();
            totalClassesSkipped += sm.getTotalSkipped();
        }
    }

    public String getSkipped() {
        return skipped;
    }

    public String getFailed() {
        return failed;
    }

    public String getTotal() {
        return total;
    }

    public String getPassed() {
        return passed;
    }

    public List<SuiteModel> getSuites() {
        return suites;
    }

    public Long getTotalTime() {
        return totalTime;
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    public int getTotalClassesTests() {
        return totalClassesTests;
    }

    public int getTotalClassesFailed() {
        return totalClassesFailed;
    }

    public int getTotalClassesPassed() {
        return totalClassesPassed;
    }

    public int getTotalClassesSkipped() {
        return totalClassesSkipped;
    }
}
