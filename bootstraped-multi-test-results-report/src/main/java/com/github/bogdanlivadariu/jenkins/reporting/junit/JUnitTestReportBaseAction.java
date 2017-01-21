package com.github.bogdanlivadariu.jenkins.reporting.junit;

import com.github.bogdanlivadariu.reporting.junit.builder.JUnitReportBuilder;
import hudson.model.Action;

public abstract class JUnitTestReportBaseAction implements Action {
    protected static final String DISPLAY_NAME = "View JUnit Reports";

    protected static final String ICON_LOCATON = "/plugin/bootstraped-multi-test-results-report/junit.png";

    public String getUrlName() {
        return JUnitReportBuilder.SUITE_OVERVIEW;
    }

    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    public String getIconFileName() {
        return ICON_LOCATON;
    }
}
