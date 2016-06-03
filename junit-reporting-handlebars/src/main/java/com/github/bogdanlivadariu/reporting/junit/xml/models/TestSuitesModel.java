package com.github.bogdanlivadariu.reporting.junit.xml.models;

import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.github.bogdanlivadariu.reporting.junit.helpers.Constants;

@XmlRootElement(name = "testsuites")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestSuitesModel {
    @XmlAttribute
    private String failures;

    @XmlAttribute
    private String time;

    @XmlAttribute
    private String errors;

    @XmlAttribute
    private String tests;

    @XmlAttribute
    private String name;

    private String uniqueID;

    private String overallStatus;

    @XmlElement(name = "testsuite")
    private List<TestSuiteModel> testsuite;

    public void postProcess() {
        uniqueID = UUID.randomUUID().toString();

        if ((failures != null && Integer.parseInt(failures) > 0) || (errors != null && Integer.parseInt(errors) > 0)) {
            overallStatus = Constants.FAILED;
        } else {
            overallStatus = Constants.PASSED;
        }
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getFailures() {
        return failures == null ? "0" : failures;
    }

    public String getTime() {
        return time == null ? "0" : time.replace(",", "");
    }

    public String getErrors() {
        return errors == null ? "0" : errors;
    }

    public String getTests() {
        return tests == null ? "0" : tests;
    }

    public String getName() {
        return name;
    }

    public List<TestSuiteModel> getTestsuite() {
        return testsuite;
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public void setTestsuite(List<TestSuiteModel> testsuite) {
        this.testsuite = testsuite;
    }

}
