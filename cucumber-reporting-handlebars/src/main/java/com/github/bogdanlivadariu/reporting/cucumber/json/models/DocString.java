package com.github.bogdanlivadariu.reporting.cucumber.json.models;

import com.google.gson.GsonBuilder;

import static com.google.gson.JsonParser.parseString;

public class DocString {

    private String value;

    private String contentType;

    private Integer line;

    public String getValue() {
        return new GsonBuilder().setPrettyPrinting().create().toJson(parseString(value));
    }

    public String getContentType() {
        return contentType;
    }

    public Integer getLine() {
        return line;
    }
}
