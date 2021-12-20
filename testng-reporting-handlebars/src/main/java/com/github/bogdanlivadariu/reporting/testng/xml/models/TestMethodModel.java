package com.github.bogdanlivadariu.reporting.testng.xml.models;

import java.util.UUID;

import jakarta.xml.bind.annotation.*;

@XmlRootElement(name = "test-method")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestMethodModel {
    @XmlAttribute
    private String status;

    @XmlAttribute
    private String signature;

    @XmlAttribute
    private String name;

    @XmlAttribute(name = "is-config")
    private boolean isConfig;

    @XmlAttribute(name = "duration-ms")
    private Long durationMs;

    @XmlAttribute(name = "started-at")
    private String startedAt;

    @XmlAttribute(name = "finished-at")
    private String finishedAt;

    @XmlElement(name = "reporter-output")
    private ReporterOutputModel reporterOutput;

    @XmlElement(name = "exception")
    private ExceptionModel exception;

    private String uniqueID;

    public TestMethodModel() {
        uniqueID = UUID.randomUUID().toString();
    }

    public String getStatus() {
        return status;
    }

    public String getSignature() {
        return signature;
    }

    public String getName() {
        return name;
    }

    public boolean getIsConfig() {
        return isConfig;
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

    public ReporterOutputModel getReporterOutput() {
        return reporterOutput;
    }

    public ExceptionModel getException() {
        return exception;
    }

    public String getUniqueID() {
        return uniqueID;
    }

}
