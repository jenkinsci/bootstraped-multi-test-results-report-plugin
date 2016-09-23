package com.github.bogdanlivadariu.reporting.rspec.xml.models;

import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.github.bogdanlivadariu.reporting.rspec.helpers.Constants;

@XmlRootElement(name = "testsuites")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestSuitesModel {

    @XmlAttribute
    private String failures = "0";

    @XmlAttribute
    private String skipped = "0";

    @XmlAttribute
    private String tests = "0";

    @XmlAttribute
    private String errors = "0";

    @XmlAttribute
    private String time;

    @XmlAttribute
    private String timestamp;

    private String uniqueID;

    private String overallStatus;

    @XmlElement(name = "testsuite")
    private List<TestSuiteModel> testsuites;

    public String getErrors() {
        return errors;
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public List<TestSuiteModel> getTestsuites() {
        return testsuites;
    }

    public String getFailures() {
        return failures;
    }

    public String getSkipped() {
        return skipped;
    }

    public String getTests() {
        return tests;
    }

    public String getTime() {
        return time;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public List<TestSuiteModel> getTestSuites() {
        return testsuites;
    }

    public void postProcess() {
        uniqueID = UUID.randomUUID().toString();
        for (TestSuiteModel ts : getTestSuites()) {
            ts.postProcess();
        }
        if (Integer.parseInt(failures) > 0) {
            overallStatus = Constants.FAILED;
        } else {
            overallStatus = Constants.PASSED;
        }
    }
}
