package com.github.bogdanlivadariu.reporting.cucumber.json.models;


public class Step {
    
    private String name;
    
    private int line;
    
    private String keyword;
    
    private boolean hidden;
    
    private Result result;
    
    private Match match;
    
    private int[] matchedColumns;
    
    private Row[] rows;
    
    private Embedding[] embeddings;

    public String getName() {
        return name;
    }

    public int getLine() {
        return line;
    }

    public String getKeyword() {
        return keyword;
    }

    public boolean isHidden() {
        return hidden;
    }

    public Result getResult() {
        return result;
    }

    public Object getMatch() {
        return match;
    }

    public int[] getMatchedColumns() {
        return matchedColumns;
    }

    public Row[] getRows() {
        return rows;
    }

    public Embedding[] getEmbeddings() {
        return embeddings;
    }

}
