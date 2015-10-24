package com.github.bogdanlivadariu.reporting.testng.xml.models;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import org.testng.reporters.XMLReporterConfig;

@XmlRootElement(name = "suite")
@XmlAccessorType(XmlAccessType.FIELD)
public class SuiteModel {

    @XmlAttribute
    private String name;

    @XmlAttribute(name = "duration-ms")
    private Long durationMs;

    @XmlAttribute(name = "started-at")
    private String startedAt;

    @XmlAttribute(name = "finished-at")
    private String finishedAt;

    @XmlElement(name = "test")
    private List<TestModel> tests;

    @XmlElementWrapper(name = "groups")
    @XmlElement(name = "group")
    private List<GroupModel> groups;

    private int totalPassed = 0;

    private int totalFailed = 0;

    private int totalSkipped = 0;

    private int totalTests = 0;

    private String overallStatus;

    private String uniqueID;

    private int totalClasses = 0;

    private LinkedHashMap<GroupModel, ArrayList<ClassModel>> groupedTestMethods;

    public void postProcess() {
        groupedTestMethods = new LinkedHashMap<GroupModel, ArrayList<ClassModel>>();
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

    public LinkedHashMap<GroupModel, ArrayList<ClassModel>> getGroupedTestMethods() {
        return groupedTestMethods;
    }

    public int getTotalClasses() {
        return totalClasses;
    }

    @SuppressWarnings("unchecked")
    public static <T extends List< ? >> T cast(Object obj) {
        return (T) obj;
    }
}
