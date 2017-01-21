package com.github.bogdanlivadariu.jenkins.reporting.junit;

import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.Publisher;

public class JUnitTestReportBuildStepDescriptor extends BuildStepDescriptor<Publisher> {

    @Override
    public String getDisplayName() {
        return "Publish JUnit reports generated with handlebars";
    }

    @Override public boolean isApplicable(Class<? extends AbstractProject> aClass) {
        return true;
    }
}

