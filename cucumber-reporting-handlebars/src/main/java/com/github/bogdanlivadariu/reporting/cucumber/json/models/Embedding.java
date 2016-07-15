package com.github.bogdanlivadariu.reporting.cucumber.json.models;

/**
 * Represents an attachment/embedding of a step.
 */
public class Embedding {

    private String mimeType;

    private String data;

    public String getMimeType() {
        return mimeType;
    }

    public String getData() {
        return data;
    }
}
