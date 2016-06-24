package com.github.bogdanlivadariu.reporting.cucumber.json.models;

public class Result {

    private long duration;

    private String status;

    private String errorMessage;

    public long getDuration() {
        return duration;
    }

    public String getStatus() {
        return status;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
