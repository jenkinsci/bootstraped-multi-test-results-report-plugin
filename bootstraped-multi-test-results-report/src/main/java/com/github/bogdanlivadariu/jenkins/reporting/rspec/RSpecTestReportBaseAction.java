package com.github.bogdanlivadariu.jenkins.reporting.rspec;

import com.github.bogdanlivadariu.reporting.rspec.builder.RSpecReportBuilder;
import hudson.model.Action;

public abstract class RSpecTestReportBaseAction implements Action {
    protected static final String ICON_LOCATON = "/plugin/bootstraped-multi-test-results-report/rspec.png";

    public String getUrlName() {
        return RSpecReportBuilder.SUITES_OVERVIEW;
    }

    public String getDisplayName() {
        return "View RSpec Reports";
    }

    public String getIconFileName() {
        return ICON_LOCATON;
    }
}
