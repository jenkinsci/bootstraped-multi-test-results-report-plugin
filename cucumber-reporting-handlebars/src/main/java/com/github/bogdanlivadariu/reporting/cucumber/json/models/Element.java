package com.github.bogdanlivadariu.reporting.cucumber.json.models;

import com.github.bogdanlivadariu.reporting.cucumber.helpers.SpecialProperties;
import com.github.bogdanlivadariu.reporting.cucumber.helpers.SpecialProperties.SpecialKeyProperties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static com.github.bogdanlivadariu.reporting.cucumber.helpers.Constants.*;

/**
 * Represents a Scenario
 */
public class Element {

    private String name;

    private String id;

    private Integer line;

    private String keyword;

    private String description;

    private String type;

    private Tag[] tags;

    private Step[] steps;

    private Step[] before;

    private Step[] after;

    private long totalDuration;

    private String overallStatus = PASSED;

    private Integer stepsPassedCount = 0;

    private Integer stepsFailedCount = 0;

    private Integer stepsSkippedCount = 0;

    private Integer stepsUndefinedCount = 0;

    private String uniqueID;

    private List<Embedding> embeddings = new ArrayList<>();

    public void postProcess(SpecialProperties props) {
        uniqueID = UUID.randomUUID().toString();
        List<String> stepStatuses = new ArrayList<>();
        if (steps != null) {

            // pull embeddings from before / after

            for (Step beforeStep : getBefore()) {
                totalDuration += beforeStep.getResult().getDuration();
                if (beforeStep.getEmbeddings().length > 0) {
                    this.embeddings.addAll(Arrays.asList(beforeStep.getEmbeddings()));
                }
            }

            for (Step afterStep : getAfter()) {
                totalDuration += afterStep.getResult().getDuration();
                if (afterStep.getEmbeddings().length > 0) {
                    this.embeddings.addAll(Arrays.asList(afterStep.getEmbeddings()));
                }
            }

            for (Step step : steps) {
                totalDuration += step.getResult().getDuration();
                String actualResultStatus = step.getResult().getStatus();
                stepStatuses.add(step.getResult().getStatus());

                Embedding[] embeddings = step.getEmbeddings();
                if (embeddings != null) {
                    this.embeddings.addAll(Arrays.asList(embeddings));
                }

                switch (actualResultStatus) {
                    case PASSED:
                        stepsPassedCount++;
                        break;
                    case FAILED:
                        stepsFailedCount++;
                        break;
                    case SKIPPED:
                        stepsSkippedCount++;
                        break;
                    case UNDEFINED:
                        stepsUndefinedCount++;
                        break;
                    default:
                        break;
                }
            }
        }
        if (stepStatuses.contains(UNDEFINED)) {
            boolean ignoreUndefinedSteps =
                props.getPropertyValue(SpecialKeyProperties.IGNORE_UNDEFINED_STEPS);
            overallStatus = ignoreUndefinedSteps ? PASSED : FAILED;
        }
        if (stepStatuses.contains(FAILED) ||
            stepStatuses.contains(SKIPPED)) {
            overallStatus = FAILED;
        }
    }

    public String getOverallStatus() {
        return overallStatus;
    }

    public long getTotalDuration() {
        return totalDuration;
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

    public Integer getLine() {
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

    public Tag[] getTags() {
        if (tags == null) {
            return new Tag[0];
        }
        return tags;
    }

    public Step[] getSteps() {
        if (steps == null) {
            return new Step[0];
        }
        return steps;
    }

    public Step[] getBefore() {
        return before != null ? before : new Step[0];
    }

    public Step[] getAfter() {
        return after != null ? after : new Step[0];
    }

    public Integer getStepsSkippedCount() {
        return stepsSkippedCount;
    }

    public Integer getStepsUndefinedCount() {
        return stepsUndefinedCount;
    }

    public Integer getStepsFailedCount() {
        return stepsFailedCount;
    }

    public Integer getStepsPassedCount() {
        return stepsPassedCount;
    }

    public Integer getStepsTotalCount() {
        Integer stepTotalCount = 0;
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
