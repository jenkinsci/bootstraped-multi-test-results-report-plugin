package com.github.bogdanlivadariu.reporting.cucumber.builder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.github.bogdanlivadariu.reporting.cucumber.helpers.Constants;
import com.github.bogdanlivadariu.reporting.cucumber.json.models.Feature;
import com.github.bogdanlivadariu.reporting.cucumber.json.models.Tag;

public class AllFeatureReports {

    private String pageTitle;

    private List<Feature> features;

    private int scenariosTotal;

    private int scenariosTotalPassed;

    private int scenariosTotalFailed;

    private int stepsTotal;

    private int stepsTotalPassed;

    private int stepsTotalFailed;

    private int stepsTotalSkipped;

    private int stepsTotalUndefined;

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

            totalDuration += feature.getTotal_duration();

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

    public int getScenariosTotal() {
        return scenariosTotal;
    }

    public int getScenariosTotalPassed() {
        return scenariosTotalPassed;
    }

    public int getScenariosTotalFailed() {
        return scenariosTotalFailed;
    }

    public int getStepsTotal() {
        return stepsTotal;
    }

    public int getStepsTotalPassed() {
        return stepsTotalPassed;
    }

    public int getStepsTotalFailed() {
        return stepsTotalFailed;
    }

    public int getStepsTotalSkipped() {
        return stepsTotalSkipped;
    }

    public int getStepsTotalUndefined() {
        return stepsTotalUndefined;
    }

    public long getTotalDuration() {
        return totalDuration;
    }

    public int getFeaturesCount() {
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
