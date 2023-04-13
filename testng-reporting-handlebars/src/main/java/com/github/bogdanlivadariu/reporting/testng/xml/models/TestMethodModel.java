package com.github.bogdanlivadariu.reporting.testng.xml.models;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.UUID;

@JacksonXmlRootElement(localName = "test-method")
public class TestMethodModel {
    private final String uniqueID;
    private String status;
    private String signature;
    private String name;

    @JacksonXmlProperty(localName = "params")
    private ParamsModel params;
    @JacksonXmlProperty(localName = "is-config")
    private boolean isConfig;
    @JacksonXmlProperty(localName = "duration-ms")
    private Long durationMs;
    @JacksonXmlProperty(localName = "started-at")
    private String startedAt;
    @JacksonXmlProperty(localName = "finished-at")
    private String finishedAt;
    @JacksonXmlProperty(localName = "reporter-output")
    private ReporterOutputModel reporterOutput;
    @JacksonXmlProperty(localName = "exception")
    private ExceptionModel exception;

    @JacksonXmlProperty(localName = "depends-on-methods")
    private String dependsOnMethods;

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
