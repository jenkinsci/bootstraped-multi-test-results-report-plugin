package com.github.bogdanlivadariu.reporting.cucumber.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.github.bogdanlivadariu.reporting.cucumber.helpers.Constants;
import com.github.bogdanlivadariu.reporting.cucumber.json.models.Feature;
import com.github.bogdanlivadariu.reporting.cucumber.json.models.Tag;

public class AllFeatureReports {

    private String pageTitle;

    private List<Feature> features;

    private Integer scenariosTotal = 0;

    private Integer scenariosTotalPassed = 0;

    private Integer scenariosTotalFailed = 0;

    private Integer stepsTotal = 0;

    private Integer stepsTotalPassed = 0;

    private Integer stepsTotalFailed = 0;

    private Integer stepsTotalSkipped = 0;

    private Integer stepsTotalUndefined = 0;

    private long totalDuration;

    private Set<String> allTags = new LinkedHashSet<String>();

    public AllFeatureReports(String pageTitle, List<Feature> features) {
        this.features = features;
        this.pageTitle = pageTitle;
        for (Feature feature : this.features) {
            scenariosTotal += feature.getElements().length;
            scenariosTotalPassed += feature.getScenariosPassedCount();
            scenariosTotalFailed += feature.getScenariosFailedCount();

            stepsTotal += feature.getStepsTotalCount();
            stepsTotalPassed += feature.getStepsPassedCount();
            stepsTotalFailed += feature.getStepsFailedCount();
            stepsTotalSkipped += feature.getStepsSkippedCount();
            stepsTotalUndefined += feature.getStepsUndefinedCount();

            totalDuration += feature.getTotalDuration();

            if (feature.getTags().length < 1) {
                allTags.add(Constants.UNTAGGED);
            } else {
                for (Tag tag : feature.getTags()) {
                    allTags.add(tag.getName());
                }
            }
        }
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public Integer getScenariosTotal() {
        return scenariosTotal;
    }

    public Integer getScenariosTotalPassed() {
        return scenariosTotalPassed;
    }

    public Integer getScenariosTotalFailed() {
        return scenariosTotalFailed;
    }

    public Integer getStepsTotal() {
        return stepsTotal;
    }

    public Integer getStepsTotalPassed() {
        return stepsTotalPassed;
    }

    public Integer getStepsTotalFailed() {
        return stepsTotalFailed;
    }

    public Integer getStepsTotalSkipped() {
        return stepsTotalSkipped;
    }

    public Integer getStepsTotalUndefined() {
        return stepsTotalUndefined;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public Integer getFeaturesCount() {
        return features.size();
    }

    public String getPageTitle() {
        return pageTitle;
    }

    public List<String> getAllTags() {
        // Collections.sort(allTags);
        List<String> sortegTags = new ArrayList<String>(allTags);
        Collections.sort(sortegTags);
        return sortegTags;
    }

}
