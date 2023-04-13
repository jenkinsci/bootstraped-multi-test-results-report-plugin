package com.github.bogdanlivadariu.reporting.junit.xml.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import com.github.bogdanlivadariu.reporting.junit.helpers.Constants;

import java.util.UUID;

import static com.github.bogdanlivadariu.reporting.junit.xml.models.BaseModel.ErrorModel;
import static com.github.bogdanlivadariu.reporting.junit.xml.models.BaseModel.FailureModel;

//@XmlRootElement(name = "testcase")
//@XmlAccessorType(XmlAccessType.FIELD)
@JacksonXmlRootElement(localName = "testcase")
public class TestCaseModel {
    //    @XmlElement(name = "system-out")
    private String systemOut;

    private String time;

    private FailureModel failure;

    private String classname;

    private String name;

    //    @XmlElement(name = "system-err")
    private String systemErr;

    //    @XmlElement(name = "error")
    private ErrorModel error;

    private String overallStatus;

    private String uniqueID;

    public ErrorModel getError() {
        return error;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public void postProcess() {
        if (failure != null) {
            overallStatus = Constants.FAILED;
        } else if (error != null) {
            overallStatus = Constants.ERRORED;
        } else {
            overallStatus = Constants.PASSED;
        }
        uniqueID = UUID.randomUUID().toString();
    }

    public String getSystemOut() {
        return systemOut;
    }

    public String getTime() {
        return time.replace(",", "");
    }

    public FailureModel getFailure() {
        return failure;
    }

    public String getClassname() {
        return classname;
    }

    public String getName() {
        return name;
    }

    public String getSystemErr() {
        return systemErr;
    }

    public String getOverallStatus() {
        return overallStatus;
    }

}
