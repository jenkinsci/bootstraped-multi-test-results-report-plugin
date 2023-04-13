package com.github.bogdanlivadariu.reporting.testng.xml.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import org.testng.reporters.XMLReporterConfig;

import java.util.*;

@JacksonXmlRootElement(localName = "suite")
public class SuiteModel {

    @JacksonXmlElementWrapper(localName = "test", useWrapping = false)
    @JacksonXmlProperty(localName = "test")
    private final List<TestModel> tests = new ArrayList<>();
    @JacksonXmlElementWrapper(localName = "groups")
    private final List<GroupModel> groups = new ArrayList<>();
    private String name;
    @JacksonXmlProperty(localName = "duration-ms")
    private Long durationMs;
    @JacksonXmlProperty(localName = "started-at")
    private String startedAt;
    @JacksonXmlProperty(localName = "finished-at")
    private String finishedAt;
    private int totalPassed = 0;

    private int totalFailed = 0;

    private int totalSkipped = 0;

    private int totalTests = 0;

    private String overallStatus;

    private String uniqueID;

    private int totalClasses = 0;

    private LinkedHashMap<GroupModel, ArrayList<ClassModel>> groupedTestMethods;

//    @SuppressWarnings("unchecked")
//    public static <T extends List<?>> T cast(Object obj) {
//        return (T) obj;
//    }

    public void postProcess() {
        groupedTestMethods = new LinkedHashMap<>();
        uniqueID = UUID.randomUUID().toString();
        for (TestModel tm : getTests()) {
            tm.postProcess();
            totalPassed += tm.getTotalPassed();
            totalFailed += tm.getTotalFailed();
            totalSkipped += tm.getTotalSkipped();
            totalClasses += tm.getClasses().size();
        }
        if (totalFailed > 0 || totalSkipped > 0) {
            overallStatus = XMLReporterConfig.TEST_FAILED;
        }
        totalTests = totalPassed + totalFailed + totalSkipped;
    }

    public String getName() {
        return name;
    }

    public Long getDurationMs() {
        return durationMs;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public String getFinishedAt() {
        return finishedAt;
    }

    public List<TestModel> getTests() {
        return tests;
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

    public List<GroupModel> getGroups() {
        return groups;
    }

    public int getTotalTests() {
        return totalTests;
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public Map<GroupModel, ArrayList<ClassModel>> getGroupedTestMethods() {
        return groupedTestMethods;
    }

    public int getTotalClasses() {
        return totalClasses;
    }
}
