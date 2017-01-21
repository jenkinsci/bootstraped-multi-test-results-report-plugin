package com.github.bogdanlivadariu.jenkins.reporting.cucumber;

import com.github.bogdanlivadariu.reporting.cucumber.builder.CucumberReportBuilder;
import hudson.model.Action;

public abstract class CucumberTestReportBaseAction implements Action {
    protected static final String DISPLAY_NAME = "View Cucumber Reports";

    protected static final String ICON_LOCATON = "/plugin/bootstraped-multi-test-results-report/cucumber.png";

    public String getUrlName() {
        return CucumberReportBuilder.FEATURES_OVERVIEW_HTML;
    }

    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    public String getIconFileName() {
        return ICON_LOCATON;
    }
}
