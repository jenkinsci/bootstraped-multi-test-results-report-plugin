package com.github.bogdanlivadariu.reporting.junit.xml.models;

import java.util.ArrayList;
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

    private Boolean hasMissingAttributes() {
        return failures == null || time == null || errors == null || tests == null;
    }

    public void postProcess() {
        uniqueID = UUID.randomUUID().toString();

        if (hasMissingAttributes()) {
            int failuresCount = 0;
            int errorsCount = 0;
            Double totalTime = 0.0;

            for (TestSuiteModel suite : getTestsuite()) {
                if (suite.getOverallStatus().equals(Constants.FAILED)) {
                    failuresCount++;
                } else if (suite.getOverallStatus().equals(Constants.ERRORED)) {
                    errorsCount++;
                }
                totalTime += Double.parseDouble(suite.getTime());
            }

            // update fields if necessary
            if (failures == null) {
                failures = Integer.toString(failuresCount);
            }
            if (tests == null) {
                tests = Integer.toString(getTestsuite().size());
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

    public String getName() {
        return name;
    }

    public List<TestSuiteModel> getTestsuite() {
        return testsuite == null ? new ArrayList<TestSuiteModel>() : testsuite;
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public void setTestsuite(List<TestSuiteModel> testsuite) {
        this.testsuite = testsuite;
    }

}
