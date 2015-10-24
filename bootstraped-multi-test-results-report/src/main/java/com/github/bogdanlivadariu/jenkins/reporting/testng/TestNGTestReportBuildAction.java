package com.github.bogdanlivadariu.jenkins.reporting.testng;

import hudson.model.AbstractBuild;

import java.io.File;

public class TestNGTestReportBuildAction extends TestNGTestReportBaseAction {

    private final AbstractBuild<?, ?> build;

    public TestNGTestReportBuildAction(AbstractBuild<?, ?> build) {
        this.build = build;
    }

    @Override
    protected String getTitle() {
        return this.build.getDisplayName() + " html3";
    }

    @Override
    protected File dir() {
        return new File(build.getRootDir(), "testng-reports-with-handlebars");
    }

}
