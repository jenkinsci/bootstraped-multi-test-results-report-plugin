package com.github.bogdanlivadariu.jenkins.reporting.testng;

import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;

public class TestNGTestReportBuildStepDescriptor extends BuildStepDescriptor<Publisher> {

    @Override
    public String getDisplayName() {
        return "View TestNG Reports";
    }

    @Override
    public boolean isApplicable(Class<? extends AbstractProject> jobType) {
        return true;
    }
}
