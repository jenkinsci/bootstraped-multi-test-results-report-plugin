package com.github.bogdanlivadariu.jenkins.reporting.rspec;

import hudson.model.AbstractBuild;

import java.io.File;

public class RSpecTestReportBuildAction extends RSpecTestReportBaseAction {

    private final AbstractBuild<?, ?> build;

    public RSpecTestReportBuildAction(AbstractBuild<?, ?> build) {
        this.build = build;
    }

    @Override
    protected String getTitle() {
        return this.build.getDisplayName() + " html3";
    }

    @Override
    protected File dir() {
        return new File(build.getRootDir(), "rspec-reports-with-handlebars");
    }

}
