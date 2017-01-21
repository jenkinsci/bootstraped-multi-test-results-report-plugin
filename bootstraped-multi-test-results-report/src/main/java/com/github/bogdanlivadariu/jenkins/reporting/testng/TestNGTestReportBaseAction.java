package com.github.bogdanlivadariu.jenkins.reporting.testng;

import com.github.bogdanlivadariu.reporting.testng.builder.TestNgReportBuilder;
import hudson.model.Action;

public abstract class TestNGTestReportBaseAction implements Action {
    protected static final String ICON_LOCATON = "/plugin/bootstraped-multi-test-results-report/testng.png";

    protected static final String DISPLAY_NAME = "View TestNG Reports";

    @Override
    public String getUrlName() {
        return TestNgReportBuilder.TESTS_BY_CLASS_OVERVIEW;
    }

    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
    }

    @Override
    public String getIconFileName() {
        return ICON_LOCATON;
    }
}
