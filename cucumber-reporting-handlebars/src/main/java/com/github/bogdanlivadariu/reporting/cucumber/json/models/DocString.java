package com.github.bogdanlivadariu.reporting.cucumber.json.models;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;

public class DocString {

    private String value;

    private String contentType;

    private Integer line;

    public String getValue() {
        JsonParser jp = new JsonParser();
        return new GsonBuilder().setPrettyPrinting().create().toJson(jp.parse(value));
    }

    public String getContentType() {
        return contentType;
    }

    public Integer getLine() {
        return line;
    }
}
