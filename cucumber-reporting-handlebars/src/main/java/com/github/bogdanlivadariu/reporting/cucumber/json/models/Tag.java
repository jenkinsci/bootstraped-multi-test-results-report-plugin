package com.github.bogdanlivadariu.reporting.cucumber.json.models;

/**
 * Represents a tag for a {@link Feature} or {@link Element}
 */
public class Tag {

    private String name;

    private int line;

    public String getName() {
        return name;
    }

    public int getLine() {
        return line;
    }
}
