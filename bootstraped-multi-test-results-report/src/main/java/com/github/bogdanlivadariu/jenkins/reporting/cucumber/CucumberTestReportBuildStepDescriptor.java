package com.github.bogdanlivadariu.jenkins.reporting.cucumber;

import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;

public class CucumberTestReportBuildStepDescriptor extends BuildStepDescriptor<Publisher> {

    @Override
    public String getDisplayName() {
        return "View Cucumber Reports";
    }

    @Override
    public boolean isApplicable(Class<? extends AbstractProject> jobType) {
        return true;
    }
}
