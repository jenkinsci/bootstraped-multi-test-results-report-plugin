package com.github.bogdanlivadariu.reporting.rspec.xml.models;

import java.util.List;
import java.util.UUID;

import jakarta.xml.bind.annotation.*;

import com.github.bogdanlivadariu.reporting.rspec.helpers.Constants;

@XmlRootElement(name = "testsuite")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestSuiteModel {
    @XmlAttribute
    private String failures = "0";

    @XmlAttribute
    private String time;

    @XmlAttribute
    private String errors = "0";

    @XmlAttribute
    private String tests = "0";

    @XmlAttribute
    private String skipped = "0";

    @XmlAttribute
    private String name;

    private String uniqueID;

    private String overallStatus;

    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    private List<PropertyModel> properties;

    @XmlElement(name = "testcase")
    private List<TestCaseModel> testcase;

    public void postProcess() {
        if (Integer.parseInt(failures) > 0) {
            overallStatus = Constants.FAILED;
        } else {
            overallStatus = Constants.PASSED;
        }
        for (TestCaseModel tc : testcase) {
            tc.postProcess();
        }
        uniqueID = UUID.randomUUID().toString();
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getFailures() {
        return failures;
    }

    public String getTime() {
        Double val = 0.0;
        for (TestCaseModel tc : getTestcase()) {
            val += Double.parseDouble(tc.getTime());
        }
        return val.toString();
    }

    public String getErrors() {
        return errors;
    }

    public String getTests() {
        return tests;
    }

    public String getSkipped() {
        return skipped;
    }

    public String getName() {
        return name;
    }

    public List<PropertyModel> getProperties() {
        return properties;
    }

    public List<TestCaseModel> getTestcase() {
        return testcase;
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public void setProperties(List<PropertyModel> properties) {
        this.properties = properties;
    }

    public void setTestcase(List<TestCaseModel> testcase) {
        this.testcase = testcase;
    }

}
