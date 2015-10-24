package com.github.bogdanlivadariu.jenkins.reporting.junit;

import hudson.model.AbstractBuild;

import java.io.File;

public class JunitTestReportBuildAction extends JUnitTestReportBaseAction {

    private final AbstractBuild<?, ?> build;

    public JunitTestReportBuildAction(AbstractBuild<?, ?> build) {
        this.build = build;
    }

    @Override
    protected String getTitle() {
        return this.build.getDisplayName() + " html3";
    }

    @Override
    protected File dir() {
        return new File(build.getRootDir(), "junit-reports-with-handlebars");
    }

}
