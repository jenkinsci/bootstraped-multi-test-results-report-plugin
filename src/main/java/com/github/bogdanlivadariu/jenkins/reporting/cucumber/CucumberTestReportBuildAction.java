package com.github.bogdanlivadariu.jenkins.reporting.cucumber;

import hudson.model.AbstractBuild;

import java.io.File;

public class CucumberTestReportBuildAction extends CucumberTestReportBaseAction {

    private final AbstractBuild<?, ?> build;

    public CucumberTestReportBuildAction(AbstractBuild<?, ?> build) {
        this.build = build;
    }

    @Override
    protected String getTitle() {
        return this.build.getDisplayName() + " html3";
    }

    @Override
    protected File dir() {
        return new File(build.getRootDir(), "cucumber-reports-with-handlebars");
    }

}
