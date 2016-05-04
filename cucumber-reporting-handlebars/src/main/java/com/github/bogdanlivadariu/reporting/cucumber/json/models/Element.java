package com.github.bogdanlivadariu.reporting.cucumber.json.models;

import static com.github.bogdanlivadariu.reporting.cucumber.helpers.Constants.FAILED;
import static com.github.bogdanlivadariu.reporting.cucumber.helpers.Constants.PASSED;
import static com.github.bogdanlivadariu.reporting.cucumber.helpers.Constants.SKIPPED;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Element {

    private String name;

    private String id;

    private int line;

    private String keyword;

    private String description;

    private String type;

    private Step[] steps;

    private long total_duration;

    private String overallStatus = "passed";

    private int stepsPassedCount;

    private int stepsFailedCount;

    private int stepsSkippedCount;

    private String uniqueID;

    private List<Embedding> embeddings;

    public void postProcess() {
        uniqueID = UUID.randomUUID().toString();
        List<String> stepStatuses = new ArrayList<>();
        if (steps != null) {

            for (Step step : steps) {
                total_duration += step.getResult().getDuration();
                String actualResultStatus = step.getResult().getStatus();
                stepStatuses.add(step.getResult().getStatus());
                if (actualResultStatus.equals(PASSED)) {
                    stepsPassedCount++;
                } else if (actualResultStatus.equals(FAILED)) {
                    stepsFailedCount++;
                } else if (actualResultStatus.equals(SKIPPED)) {
                    stepsSkippedCount++;
                }
            }
        }
        if (stepStatuses.contains(FAILED) || stepStatuses.contains(SKIPPED)) {
            overallStatus = "failed";
        }
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public long getTotal_duration() {
        return total_duration;
    }

    public void appendEmbedding(Embedding embedding) {
        if (embeddings == null) {
            embeddings = new ArrayList<Embedding>();
        }
        embeddings.add(embedding);
    }

    public List<Embedding> getEmbeddings() {
        return embeddings;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public int getLine() {
        return line;
    }

    public String getKeyword() {
        return keyword;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public Step[] getSteps() {
        return steps;
    }

    public int getStepsSkippedCount() {
        return stepsSkippedCount;
    }

    public int getStepsFailedCount() {
        return stepsFailedCount;
    }

    public int getStepsPassedCount() {
        return stepsPassedCount;
    }

    public int getStepsTotalCount() {
        int stepTotalCount = 0;
        try {
            stepTotalCount = steps.length;
        } catch (Exception e) {
            stepTotalCount = 0;
        }
        return stepTotalCount;
    }

    public String getUniqueID() {
        return uniqueID;
    }
}
