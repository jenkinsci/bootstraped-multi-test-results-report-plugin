package com.github.bogdanlivadariu.reporting.cucumber.json.models;

/**
 * Represents a step of a scenario.
 */
public class Step {

    private String name;

    private Integer line;

    private String keyword;

    private Boolean hidden;

    private Result result;

    private Match match;
    
    private DocString docString;

    private Integer[] matchedColumns;

    private Row[] rows;

    private Embedding[] embeddings;

    public String getName() {
        return name;
    }

    public Integer getLine() {
        return line;
    }

    public String getKeyword() {
        return keyword;
    }

    public Boolean isHidden() {
        return hidden;
    }

    public Result getResult() {
        return result;
    }

    public Object getMatch() {
        return match;
    }

    public Integer[] getMatchedColumns() {
        return matchedColumns;
    }

    public Row[] getRows() {
        return rows;
    }

    public Embedding[] getEmbeddings() {
        if (embeddings == null) {
            return new Embedding[0];
        }
        return embeddings;
    }

    public Boolean getHidden() {
        return hidden;
    }

    public DocString getDocString() {
        return docString;
    }

}
