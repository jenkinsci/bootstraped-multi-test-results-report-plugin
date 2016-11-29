package com.github.bogdanlivadariu.reporting.junit.xml.models;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.github.bogdanlivadariu.reporting.junit.helpers.Constants;

@XmlRootElement(name = "testsuite")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestSuiteModel {
    @XmlAttribute
    private String failures;

    @XmlAttribute
    private String time;

    @XmlAttribute
    private String errors;

    @XmlAttribute
    private String tests;

    @XmlAttribute
    private String skipped;

    @XmlAttribute
    private String name;

    private String uniqueID;

    private String overallStatus;

    @XmlElementWrapper(name = "properties")
    @XmlElement(name = "property")
    private List<PropertyModel> properties;

    @XmlElement(name = "testcase")
    private List<TestCaseModel> testcase;

    private Boolean hasMissingAttributes() {
        return failures == null || time == null || errors == null || tests == null || skipped == null;
    }

    public void postProcess() {
        uniqueID = UUID.randomUUID().toString();

        if (hasMissingAttributes()) {
            int failuresCount = 0;
            int skippedCount = 0;
            int errorsCount = 0;
            Double totalTime = 0.0;

            for (TestCaseModel test : getTestcase()) {
                if (test.getOverallStatus().equals(Constants.FAILED)) {
                    failuresCount++;
                } else if (test.getOverallStatus().equals(Constants.ERRORED)) {
                    errorsCount++;
                } else if (test.getOverallStatus().equals(Constants.SKIPPED)) {
                    skippedCount++;
                }
                totalTime += Double.parseDouble(test.getTime());
            }

            // update fields if necessary
            if (failures == null) {
                failures = Integer.toString(failuresCount);
            }
            if (skipped == null) {
                skipped = Integer.toString(skippedCount);
            }
            if (tests == null) {
                tests = Integer.toString(getTestcase().size());
            }
            if (time == null) {
                time = Double.toString(totalTime);
            }
            if (errors == null) {
                errors = Integer.toString(errorsCount);
            }
        }

        if (Integer.parseInt(failures) > 0 || Integer.parseInt(errors) > 0) {
            overallStatus = Constants.FAILED;
        } else {
            overallStatus = Constants.PASSED;
        }
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getFailures() {
        return failures;
    }

    public String getTime() {
        return time.replace(",", "");
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
        return properties == null ? new ArrayList<PropertyModel>() : properties;
    }

    public List<TestCaseModel> getTestcase() {
        return testcase == null ? new ArrayList<TestCaseModel>() : testcase;
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
