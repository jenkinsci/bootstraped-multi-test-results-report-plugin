package com.github.bogdanlivadariu.reporting.cucumber.json.models;

import static com.github.bogdanlivadariu.reporting.cucumber.helpers.Constants.FAILED;
import static com.github.bogdanlivadariu.reporting.cucumber.helpers.Constants.PASSED;
import static com.github.bogdanlivadariu.reporting.cucumber.helpers.Constants.SKIPPED;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.github.bogdanlivadariu.reporting.cucumber.helpers.Constants;

public class Feature {

    private String pageTitle;

    private String uniqueID;

    private String id;

    private String name;

    private String description;

    private int line;

    private String keyword;

    private Tag[] tags;

    private String uri;

    private Element[] elements;

    private int zbang;

    private long total_duration;

    private String overall_status = PASSED;

    private int scenariosPassedCount;

    private int scenariosFailedCount;

    private int stepsTotalCount;

    private int stepsPassedCount;

    private int stepsFailedCount;

    private int stepsSkippedCount;

    private String outputFileLocation;

    public String getOutputFileLocation() {
        return outputFileLocation;
    }

    public void setOutputFileLocation(String outputFileLocation) {
        this.outputFileLocation = outputFileLocation;
    }

    public Feature postProcess() {
        pageTitle = Constants.FEATURE_SUMMARY_REPORT;
        uniqueID = UUID.randomUUID().toString();
        outputFileLocation = "feature-reports/" + uniqueID + ".html";
        List<String> stepResultStatuses = new ArrayList<>();
        for (Element el : elements) {
            el.postProcess();
            total_duration += el.getTotal_duration();
            stepsTotalCount += el.getStepsTotalCount();
            stepsPassedCount += el.getStepsPassedCount();
            stepsFailedCount += el.getStepsFailedCount();
            stepsSkippedCount += el.getStepsSkippedCount();

            if (el.getOverallStatus().equals(PASSED)) {
                scenariosPassedCount++;
            } else {
                scenariosFailedCount++;
            }
            if (el.getSteps() != null) {
                for (Step step : el.getSteps()) {
                    fillStepResultStatuses(stepResultStatuses, el, step);
                }
            }
        }
        if (stepResultStatuses.contains(FAILED) || stepResultStatuses.contains(SKIPPED)) {
            overall_status = FAILED;
        }
        return this;
    }

    private void fillStepResultStatuses(List<String> stepResultStatuses, Element el, Step step) {
        stepResultStatuses.add(step.getResult().getStatus());
        try {
            for (Embedding emb : step.getEmbeddings()) {
                if (emb == null) {
                    continue;
                }
                el.appendEmbedding(emb);
            }
        } catch (Exception e) {
            // an exception appeared when extracting the embeddings
        }
    }

    public int getScenariosCount() {
        return elements.length;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getLine() {
        return line;
    }

    public String getKeyword() {
        return keyword;
    }

    public Tag[] getTags() {
        return tags == null ? new Tag[] {} : tags;
    }

    public String getUri() {
        return uri;
    }

    public Element[] getElements() {
        return elements;
    }

    public int getZbang() {
        return zbang;
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

    public int getScenariosFailedCount() {
        return scenariosFailedCount;
    }

    public int getStepsTotalCount() {
        return stepsTotalCount;
    }

    public long getTotal_duration() {
        return total_duration;
    }

    public int getScenariosPassedCount() {
        return scenariosPassedCount;
    }

    public String getOverall_status() {
        return overall_status;
    }

    public String getUniqueID() {
        return uniqueID;
    }

    public String getPageTitle() {
        return pageTitle;
    }

}
